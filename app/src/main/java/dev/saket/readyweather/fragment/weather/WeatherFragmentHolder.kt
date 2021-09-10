package dev.saket.readyweather.fragment.weather

import androidx.recyclerview.widget.RecyclerView
import dev.saket.readyweather.databinding.ItemHourlySheetBinding
import com.squareup.picasso.Picasso
import dev.saket.readyweather.R
import dev.saket.readyweather.data.remote.Constants
import dev.saket.readyweather.data.remote.openWeather.entity.Hourly
import dev.saket.readyweather.utils.DateFormat
import dev.saket.readyweather.utils.DateHelper.unixSecondsToText

class WeatherFragmentHolder(private val binding: ItemHourlySheetBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun hourlySheetData(hourly: Hourly) {
        with(binding) {
            timeHourlyTV.text = hourly.date.unixSecondsToText(DateFormat.TIME_12HOUR, true)
            tempHourlyTV.text =
                binding.root.context.getString(R.string.temp_data_string, hourly.temp)

            val iconCodeRV = hourly.weather[0].icon

            Picasso.with(root.context)
                .load(Constants.IMAGE_URL_BASE + iconCodeRV + Constants.IMAGE_FORMAT)
                .into(weatherHourlyIV)
        }
    }

}