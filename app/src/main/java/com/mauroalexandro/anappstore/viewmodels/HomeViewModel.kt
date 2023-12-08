package com.mauroalexandro.anappstore.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mauroalexandro.anappstore.models.AppList
import com.mauroalexandro.anappstore.network.ApiClient
import com.mauroalexandro.anappstore.network.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    init {
        getAppList()
    }
    private var apiClient: ApiClient = ApiClient.getInstance()
    private var jobScope: Job? = null
    private val appList = MutableLiveData<Resource<AppList>>()

    /**
     * Get App List to show on Main Screen
     */
    private fun getAppList(){
        jobScope = CoroutineScope(Dispatchers.IO).launch {
            appList.postValue(Resource.loading(null))

            apiClient.getClient()?.getApps()?.enqueue(object :
                Callback<AppList> {
                override fun onResponse(
                    call: Call<AppList>,
                    response: Response<AppList>
                ) {
                    val appListResponse: AppList? = response.body()

                    if (appListResponse != null) {
                        appList.postValue(Resource.success(appListResponse))
                    }
                }

                override fun onFailure(call: Call<AppList>, t: Throwable) {
                    appList.postValue(t.message?.let { Resource.error(it, null) })
                    call.cancel()
                }
            })
        }
    }

    fun getAppsFromService(): MutableLiveData<Resource<AppList>> {
        return appList
    }

    override fun onCleared() {
        super.onCleared()
        jobScope?.cancel()
    }
}