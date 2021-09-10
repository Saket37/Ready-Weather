package dev.saket.readyweather.main

import android.util.Log
import androidx.lifecycle.*
import dev.saket.readyweather.data.Repository
import dev.saket.readyweather.data.remote.Location
import dev.saket.readyweather.data.remote.Resource
import dev.saket.readyweather.data.remote.Status
import dev.saket.readyweather.data.remote.mapQuest.entity.MapQuestResult
import dev.saket.readyweather.data.remote.openWeather.entity.Current
import dev.saket.readyweather.data.remote.openWeather.entity.Daily
import dev.saket.readyweather.data.remote.openWeather.entity.Hourly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _currentWeatherData = MutableLiveData<Resource<Pair<Current, List<Hourly>>>>()
    val currentWeatherData: LiveData<Resource<Pair<Current, List<Hourly>>>>
        get() = _currentWeatherData

    private val _dailyWeatherData = MutableLiveData<Resource<List<Daily>>>()
    val dailyWeatherData: LiveData<Resource<List<Daily>>>
        get() = _dailyWeatherData

    private val _nextDayHourlyWeatherData =
        MutableLiveData<Resource<Pair<List<Daily>, List<Hourly>>>>()
    val nextDayHourlyWeatherData: LiveData<Resource<Pair<List<Daily>, List<Hourly>>>>
        get() = _nextDayHourlyWeatherData

    private val _mapQuestData = MutableLiveData<Resource<MapQuestResult>>()
    val mapQuestData: LiveData<Resource<MapQuestResult>>
        get() = _mapQuestData

    @ExperimentalCoroutinesApi
    private val locationFlow = MutableStateFlow<Location?>(null)

    init {
        callAPI()
    }

    private fun loadCurrentWeather(location: Location) = flow {
        emit(Resource.loading(null))
        try {
            val data = repository.getWeatherReport(location.latitude, location.longitude)
            emit(Resource.success(data))
        } catch (e: Exception) {
            Log.e(this@MainViewModel::class.simpleName, e.message!!)
            e.printStackTrace()
            emit(Resource.error(null, e.message ?: "Unknown error"))
        }
    }

    private suspend fun loadCityName(location: Location) {
        _mapQuestData.postValue(Resource.loading(null))
        try {
            val data = repository.getCityName(location)
            _mapQuestData.postValue(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            _mapQuestData.postValue(Resource.error(null, e.message ?: "Unknown error"))
        }
    }

    fun setLocation(location: Location) {
        Log.d("SendingLocationUpdate", location.toString())
        locationFlow.value = location
    }

    private val _networkStatus = MutableLiveData(false)
    val networkStatus: LiveData<Boolean>
        get() = _networkStatus

    fun setNetworkStatus(connected:Boolean) {
        _networkStatus.postValue(connected)
        if (connected) {
            viewModelScope.launch {
                locationFlow.value?.let {
                    callAPI()
                }
            }
        }
    }
    private fun callAPI() {
        locationFlow.flatMapLatest {
            if (it != null) {
                Log.d("ReceivedLocationUpdate", it.toString())
                loadCityName(it)
                loadCurrentWeather(it)
            } else {
                Log.d("ReceivedLocationUpdate", "null")
                flow {
                    Resource.loading(null)
                }
            }
        }.onEach {
            when (it.status) {
                Status.SUCCESS -> {
                    val currentDayData = Pair(it.data!!.current, it.data.hourly)
                    val nextDayData = Pair(it.data.daily, it.data.hourly)
                    _currentWeatherData.postValue(Resource.success(currentDayData))
                    _dailyWeatherData.postValue(Resource.success(it.data.daily))
                    _nextDayHourlyWeatherData.postValue(Resource.success(nextDayData))
                }
                Status.ERROR -> {
                    with(Resource.error(null, it.message ?: "Unknown error")) {
                        _currentWeatherData.postValue(this)
                        _dailyWeatherData.postValue(this)
                        _nextDayHourlyWeatherData.postValue(this)
                    }
                }
                Status.LOADING -> {
                    with(Resource.loading(null)) {
                        _currentWeatherData.postValue(this)
                        _dailyWeatherData.postValue(this)
                        _nextDayHourlyWeatherData.postValue(this)
                    }
                }
            }
        }.distinctUntilChanged().launchIn(viewModelScope)
    }
}