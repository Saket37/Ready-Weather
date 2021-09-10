package dev.saket.readyweather.fragment.nextday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.saket.readyweather.data.remote.openWeather.entity.Hourly
import dev.saket.readyweather.databinding.ItemHourlySheetBinding
import javax.inject.Inject

class NextDayFragmentAdapter @Inject constructor() : RecyclerView.Adapter<NextDayFragmentHolder>() {
    var nextDayHourlyResult: List<Hourly> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDayFragmentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHourlySheetBinding.inflate(inflater, parent, false)
        return NextDayFragmentHolder(binding)
    }

    override fun onBindViewHolder(holder: NextDayFragmentHolder, position: Int) {
        with(nextDayHourlyResult[position]) {
            holder.nextDayHourlySheetData(this)
        }
    }

    override fun getItemCount(): Int = nextDayHourlyResult.size

}