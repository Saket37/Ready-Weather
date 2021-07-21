package com.example.readyweather.fragment.nextday

import androidx.recyclerview.widget.RecyclerView
import com.example.readyweather.R
import com.example.readyweather.data.remote.Constants
import com.example.readyweather.data.remote.openWeather.entity.Hourly
import com.example.readyweather.databinding.ItemHourlySheetBinding
import com.example.readyweather.utils.DateFormat
import com.example.readyweather.utils.DateHelper.unixSecondsToText
import com.squareup.picasso.Picasso

class NextDayFragmentHolder(private val binding: ItemHourlySheetBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun nextDayHourlySheetData(hourly: Hourly) {
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