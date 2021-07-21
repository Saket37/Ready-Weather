package com.example.readyweather.fragment.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readyweather.data.remote.openWeather.entity.Daily
import com.example.readyweather.databinding.ItemDailyForecastBinding
import javax.inject.Inject

class ForecastFragmentAdapter @Inject constructor() :
    RecyclerView.Adapter<ForecastFragmentHolder>() {

    var dailyResult: List<Daily> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastFragmentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dailyBinding = ItemDailyForecastBinding.inflate(inflater, parent, false)
        return ForecastFragmentHolder(dailyBinding)
    }

    override fun onBindViewHolder(holder: ForecastFragmentHolder, position: Int) {
        with(dailyResult[position]) {
            holder.dailyWeatherData(this)
        }
    }

    override fun getItemCount(): Int = dailyResult.size
}