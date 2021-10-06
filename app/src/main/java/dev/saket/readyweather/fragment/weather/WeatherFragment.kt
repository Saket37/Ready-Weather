package dev.saket.readyweather.fragment.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.saket.readyweather.R
import dev.saket.readyweather.data.remote.Constants.IMAGE_FORMAT
import dev.saket.readyweather.data.remote.Constants.IMAGE_URL_BASE
import dev.saket.readyweather.data.remote.Status
import dev.saket.readyweather.data.remote.mapQuest.entity.Results
import dev.saket.readyweather.data.remote.openWeather.entity.Current
import dev.saket.readyweather.data.remote.openWeather.entity.Daily
import dev.saket.readyweather.data.remote.openWeather.entity.Hourly
import dev.saket.readyweather.databinding.WeatherBottomSheetBinding
import dev.saket.readyweather.databinding.WeatherFragmentBinding
import dev.saket.readyweather.main.MainViewModel
import dev.saket.readyweather.utils.DateFormat
import dev.saket.readyweather.utils.DateHelper.unixSecondsToText
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant

@AndroidEntryPoint
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private var _bottomSheetBinding: WeatherBottomSheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    lateinit var hourlyAdapter: WeatherFragmentAdapter
    lateinit var progressBar: ProgressBar



    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        _bottomSheetBinding = binding.weatherBottomSheet
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar

        hourlyAdapter = WeatherFragmentAdapter()

        val recyclerView = bottomSheetBinding.hourlyWeatherRV
        recyclerView.apply {
            adapter = hourlyAdapter
        }


        viewModel.mapQuestData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        progressBar.visibility = View.GONE
                        hideUILayout(View.VISIBLE)
                        displayCityName(it.data.results)
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                }
                Status.ERROR -> {
                    it.message?.let { it1 -> Log.e(this::class.java.canonicalName, it1) }
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    hideUILayout(View.GONE)
                    progressBar.visibility = View.VISIBLE
                    progressBar.isIndeterminate = true

                }
            }
        })

        viewModel.currentWeatherData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    hideUILayout(View.GONE)
                    progressBar.visibility = View.VISIBLE
                    progressBar.isIndeterminate = true
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        progressBar.visibility = View.GONE
                        hideUILayout(View.VISIBLE)
                        updateUIData(it.data.first)
                        hourlyAdapter.hourlyResult = splitHourlyDate(it.data.first, it.data.second)
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.ERROR ->
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.dailyWeatherData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        progressBar.visibility = View.GONE
                        hideUILayout(View.VISIBLE)
                        dayAndNightTempData(it.data)
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.ERROR -> Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                Status.LOADING -> {
                    hideUILayout(View.GONE)
                    progressBar.visibility = View.VISIBLE
                    progressBar.isIndeterminate = true
                }
            }
        })


    }

    private fun updateUIData(current: Current) {

        binding.tempTV.text = getString(R.string.temp_data_string, current.temp)
        binding.feelsLike.text =
            getString(R.string.feels_like_data_string, current.feelsLike)

        // TODO(set description instead of weather[0].main and split in two lines if word exceeds 3)
        try {
            binding.weatherNameTV.text = current.weather[0].main
            val iconCode = current.weather[0].icon
            Picasso.get()
                .load(IMAGE_URL_BASE + iconCode + IMAGE_FORMAT)
                .into(binding.weatherIV)
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error retrieving Weather Data", Toast.LENGTH_SHORT)
                .show()
        }


        Log.d(
            this::class.java.canonicalName,
            "Date UNIX: ${current.date}, System: ${System.currentTimeMillis() / 1000} ,Difference: ${System.currentTimeMillis() / 1000 - current.date}"
        )

        binding.dayAndDateTV.text = current.date.unixSecondsToText(DateFormat.DATE_WITH_DAY)

        val sunriseTime = current.sunrise
        val sunsetTime = current.sunset

        binding.timeTV.text = current.date.unixSecondsToText(DateFormat.TIME_12HOUR, true)
        binding.displaySunriseTV.text =
            sunriseTime.unixSecondsToText(DateFormat.TIME_12HOUR, true)
        binding.displaySunsetTV.text =
            sunsetTime.unixSecondsToText(DateFormat.TIME_12HOUR, true)
        val timeDifference = sunsetTime - sunriseTime
        val systemTime = Instant.now().epochSecond
        val differenceOfSunAndCurrent = systemTime - sunriseTime
        val duration = (differenceOfSunAndCurrent.toFloat() / timeDifference.toFloat())

        Log.d("Motion progress", "Progress is set to $duration")

        binding.motion.progress = duration


        bottomSheetBinding.displayPressureTV.text =
            getString(R.string.pressure_data_string, current.pressure)

        bottomSheetBinding.displayHumidityTV.text =
            getString(R.string.humidity_data_string, current.humidity)

        when {
            current.uvi < 3 -> {
                bottomSheetBinding.displayUviTV.text =
                    getString(R.string.low_uvi, current.uvi)

            }
            current.uvi < 8 -> {
                bottomSheetBinding.displayUviTV.text =
                    getString(R.string.moderate_uvi, current.uvi)
            }
            else -> {
                bottomSheetBinding.displayUviTV.text =
                    getString(R.string.extreme_uvi, current.uvi)
            }
        }

        bottomSheetBinding.displayVisibilityTV.text =
            getString(R.string.visibility_data_string, current.visibility)

        bottomSheetBinding.displayCloudTV.text =
            getString(R.string.cloud_data_string, current.clouds)

        bottomSheetBinding.displayWindTv.text =
            getString(R.string.wind_speed_data_string, current.windSpeed)


    }

    private fun splitHourlyDate(current: Current, hourly: List<Hourly>): List<Hourly> {
        val dataToShow = arrayListOf<Hourly>();

        val currentDate =
            current.date.unixSecondsToText(DateFormat.ONLY_DAY_OF_MONTH).toInt()

        hourly.forEach {
            val hourlyDate = it.date.unixSecondsToText(DateFormat.ONLY_DAY_OF_MONTH).toInt()
            if (currentDate == hourlyDate) {
                dataToShow.add(it)
            }
        }

        return dataToShow
    }
    // TODO add minutely pop

    private fun displayCityName(result: List<Results>) {
        Log.d(this::class.java.canonicalName, "City Name ${result[0].locations[0].adminArea5}")
        binding.cityTV.text = result[0].locations[0].adminArea5
    }

    private fun dayAndNightTempData(daily: List<Daily>) {
        binding.dayTempTV.text = getString(R.string.day_temp_data_string, daily[0].temp.day)
        binding.nightTempTV.text = getString(R.string.night_temp_data_string, daily[0].temp.night)
    }

    private fun hideUILayout(visible: Int) {
        binding.sunriseTV.visibility = visible
        binding.sunsetTV.visibility = visible
        binding.weatherBottomSheet.weatherSheet.visibility = visible
        binding.motion.visibility = visible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomSheetBinding = null
    }

}