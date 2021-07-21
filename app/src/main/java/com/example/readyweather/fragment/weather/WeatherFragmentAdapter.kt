package com.example.readyweather.fragment.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readyweather.data.remote.openWeather.entity.Hourly
import com.example.readyweather.databinding.ItemHourlySheetBinding
import javax.inject.Inject


class WeatherFragmentAdapter @Inject constructor() : RecyclerView.Adapter<WeatherFragmentHolder>() {

    var hourlyResult: List<Hourly> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherFragmentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHourlySheetBinding.inflate(inflater, parent, false)
        return WeatherFragmentHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherFragmentHolder, position: Int) {
        with(hourlyResult[position]) {
            holder.hourlySheetData(this)

        }
    }

    override fun getItemCount(): Int = hourlyResult.size


}