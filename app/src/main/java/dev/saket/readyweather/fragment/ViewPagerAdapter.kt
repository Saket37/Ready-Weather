package dev.saket.readyweather.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.saket.readyweather.fragment.forecast.ForecastFragment
import dev.saket.readyweather.fragment.nextday.NextDayFragment
import dev.saket.readyweather.fragment.weather.WeatherFragment

private const val NUM_TABS = 3

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return WeatherFragment()
            1 -> return NextDayFragment()
            2 -> return ForecastFragment()
        }
        return WeatherFragment()
    }
}