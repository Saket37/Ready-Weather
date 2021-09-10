package dev.saket.readyweather.fragment.forecast

import android.transition.TransitionManager
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dev.saket.readyweather.R
import dev.saket.readyweather.data.remote.Constants
import dev.saket.readyweather.data.remote.openWeather.entity.Daily
import dev.saket.readyweather.databinding.ItemDailyForecastBinding
import dev.saket.readyweather.utils.DateFormat
import dev.saket.readyweather.utils.DateHelper.unixSecondsToText
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ForecastFragmentHolder(private val dailyBinding: ItemDailyForecastBinding) :
    RecyclerView.ViewHolder(dailyBinding.root) {
    var check = false

    fun dailyWeatherData(daily: Daily) {
        with(dailyBinding) {

            val currentDay = LocalDate.now().format(DateTimeFormatter.ofPattern("d"))
            if (currentDay == daily.date.unixSecondsToText(DateFormat.ONLY_DAY_OF_MONTH))
                dailyDateTv.text = root.context.getString(R.string.forecast_date_string)
            else dailyDateTv.text = daily.date.unixSecondsToText(DateFormat.DATE_WITH_3_LETTER_DAY)
            try {
                dailyWeatherTV.text = daily.weather[0].main

                val dailyIconCode = daily.weather[0].icon

                Picasso.with(dailyBinding.root.context)
                    .load(Constants.IMAGE_URL_BASE + dailyIconCode + Constants.IMAGE_FORMAT)
                    .into(dailyIconIV)
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
                Toast.makeText(root.context, "Error retrieving Weather Data", Toast.LENGTH_SHORT)
                    .show()
            }
            maxTV.text =
                dailyBinding.root.context.getString(R.string.temp_data_string, daily.temp.max)

            minTV.text =
                dailyBinding.root.context.getString(R.string.temp_data_string, daily.temp.min)


            dailySunriseTV.text = daily.sunrise.unixSecondsToText(DateFormat.TIME_12HOUR, true)

            dailySunsetTV.text = daily.sunset.unixSecondsToText(DateFormat.TIME_12HOUR, true)

            dailyDisplayHumidityTV.text =
                root.context.getString(R.string.humidity_data_string, daily.humidity)

            dailyDisplayPressureTV.text =
                root.context.getString(R.string.pressure_data_string, daily.pressure)
// TODO (add wind_deg to displayWindTV)
            dailyDisplayWindTV.text =
                root.context.getString(R.string.wind_speed_data_string, daily.wind_speed)

            dailyDisplayCloudsTV.text =
                root.context.getString(R.string.cloud_data_string, daily.clouds)


            when {
                daily.uvi < 3 -> {
                    dailyDisplayUviTV.text =
                        root.context.getString(R.string.low_uvi, daily.uvi)

                }
                daily.uvi < 8 -> {
                    dailyDisplayUviTV.text =
                        root.context.getString(R.string.moderate_uvi, daily.uvi)
                }
                else -> {
                    dailyDisplayUviTV.text =
                        root.context.getString(R.string.extreme_uvi, daily.uvi)
                }
            }


            dailyDisplayPopTv.text =
                root.context.getString(R.string.chance_of_rain_data_string, daily.pop)

            dailyDisplayRainTV.text = root.context.getString(R.string.rain_data_string, daily.rain)

            showCV.setOnClickListener {
                TransitionManager.beginDelayedTransition(dailyBinding.hiddenCV)
                if (!check) {
                    dailyBinding.hiddenCV.visibility = View.VISIBLE
                } else {
                    dailyBinding.hiddenCV.visibility = View.GONE
                }
                check = !check
            }

        }
    }
}