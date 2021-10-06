package dev.saket.readyweather.fragment.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.saket.readyweather.data.remote.Status
import dev.saket.readyweather.databinding.ForecastFragmentBinding
import dev.saket.readyweather.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ForecastFragment : Fragment() {
    private var _binding: ForecastFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dailyAdapter: ForecastFragmentAdapter
    lateinit var progressBar: ProgressBar


    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar

        val recyclerView = binding.dailyRV
        recyclerView.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(context)
            //  TODO("add divider for rv")
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        }

        viewModel.dailyWeatherData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        progressBar.visibility = View.GONE
                        dailyAdapter.dailyResult = it.data
                    } else {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                Status.ERROR ->
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    progressBar.isIndeterminate = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}