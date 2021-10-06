package dev.saket.readyweather.fragment.nextday

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dev.saket.readyweather.R
import dev.saket.readyweather.data.remote.Constants
import dev.saket.readyweather.data.remote.Status
import dev.saket.readyweather.data.remote.mapQuest.entity.Results
import dev.saket.readyweather.data.remote.openWeather.entity.Daily
import dev.saket.readyweather.data.remote.openWeather.entity.Hourly
import dev.saket.readyweather.databinding.NextDayBottomSheetBinding
import dev.saket.readyweather.databinding.NextdayFragmentBinding
import dev.saket.readyweather.main.MainViewModel
import dev.saket.readyweather.utils.CapitalizeLetterHelper.capitalizeWords
import dev.saket.readyweather.utils.DateFormat
import dev.saket.readyweather.utils.DateHelper.unixSecondsToText
import java.util.*

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class NextDayFragment : Fragment() {
    private var _binding: NextdayFragmentBinding? = null
    private val binding get() = _binding!!
    private var _bottomSheetBinding: NextDayBottomSheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    lateinit var hourlyAdapter: NextDayFragmentAdapter
    lateinit var progressBar: ProgressBar

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = NextdayFragmentBinding.inflate(inflater, container, false)
        _bottomSheetBinding = binding.nextDayBottomSheet
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        hourlyAdapter = NextDayFragmentAdapter()

        val recyclerView = bottomSheetBinding.nextDayWeatherRV
        recyclerView.apply {
            adapter = hourlyAdapter
        }

        viewModel.nextDayHourlyWeatherData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        progressBar.visibility = View.GONE
                        updateUILayout(View.VISIBLE)
                        updateUI(it.data.first)
                        hourlyAdapter.nextDayHourlyResult =
                            spiltHourlyData(it.data.first, it.data.second)
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.ERROR -> Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                Status.LOADING -> {
                    updateUILayout(View.GONE)
                    progressBar.visibility = View.VISIBLE
                    progressBar.isIndeterminate = true
                }
            }
        })

        viewModel.mapQuestData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        progressBar.visibility = View.GONE
                        updateUILayout(View.VISIBLE)
                        displayCityName(it.data.results)
                    } else {
                        Toast.makeText(requireContext(), "City Error", Toast.LENGTH_SHORT).show()
                    }

                }
                Status.ERROR -> {
                    it.message?.let { it1 -> Log.e(this::class.java.canonicalName, it1) }
                    Toast.makeText(requireContext(), "City Error", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    updateUILayout(View.GONE)
                    progressBar.visibility = View.VISIBLE
                    progressBar.isIndeterminate = true
                }
            }
        })
    }

    private fun updateUI(daily: List<Daily>?) {
//binding.nextDayTempTV.text = getString(R.string.temp_string,daily[1].temp.)
        try {
            if (daily != null) {
                binding.nextDayAndDateTV.text =
                    daily[1].date.unixSecondsToText(DateFormat.DATE_WITH_DAY)

                val pop = daily[1].pop

                if (pop == 0.00) {
                    binding.chanceOfRainTV.visibility = View.GONE
                } else {
                    binding.chanceOfRainTV.text =
                        getString(R.string.chance_of_rain_data_string, pop)
                }

                val weatherIconCode = daily[1].weather[0].icon
                Picasso.get()
                    .load(Constants.IMAGE_URL_BASE + weatherIconCode + Constants.IMAGE_FORMAT)
                    .into(binding.nextDayWeatherIV)

                val description = daily[1].weather[0].description
                binding.nextDayWeatherNameTV.text = description.capitalizeWords()

                binding.dayTempTV.text = getString(R.string.day_temp_data_string, daily[1].temp.day)
                binding.nightTempTV.text =
                    getString(R.string.night_temp_data_string, daily[1].temp.night)

                bottomSheetBinding.nextDayDisplayPressureTV.text =
                    getString(R.string.pressure_data_string, daily[1].pressure)

                bottomSheetBinding.nextDayDisplayHumidityTV.text =
                    getString(R.string.humidity_data_string, daily[1].humidity)

                when {
                    daily[1].uvi < 3 -> bottomSheetBinding.nextDayDisplayUviTV.text =
                        getString(R.string.low_uvi, daily[1].uvi)

                    daily[1].uvi < 8 -> bottomSheetBinding.nextDayDisplayUviTV.text =
                        getString(R.string.moderate_uvi, daily[1].uvi)

                    else -> bottomSheetBinding.nextDayDisplayUviTV.text =
                        getString(R.string.extreme_uvi, daily[1].uvi)
                }

                bottomSheetBinding.nextDayDisplayCloudTV.text =
                    getString(R.string.cloud_data_string, daily[1].clouds)

                bottomSheetBinding.nextDayDisplayWindTv.text =
                    getString(R.string.wind_speed_data_string, daily[1].wind_speed)

            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error retrieving Weather Data", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun spiltHourlyData(daily: List<Daily>, hourly: List<Hourly>): ArrayList<Hourly> {
        val dataToShow = arrayListOf<Hourly>();

        val nextDayDate =
            daily[1].date.unixSecondsToText(DateFormat.ONLY_DAY_OF_MONTH).toInt()

        hourly.forEach {
            val hourlyDate = it.date.unixSecondsToText(DateFormat.ONLY_DAY_OF_MONTH).toInt()
            if (nextDayDate == hourlyDate) {
                dataToShow.add(it)
            }
        }

        return dataToShow
    }

    private fun displayCityName(result: List<Results>) {
        Log.d(this::class.java.canonicalName, "City Name ${result[0].locations[0].adminArea5}")
        binding.nextDayCityTV.text = result[0].locations[0].adminArea5
    }

    private fun updateUILayout(visible: Int) {
        binding.nextDayBottomSheet.nextDaySheet.visibility = visible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomSheetBinding = null
    }
}