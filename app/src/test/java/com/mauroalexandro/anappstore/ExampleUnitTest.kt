package com.mauroalexandro.anappstore

import com.mauroalexandro.anappstore.models.AppList
import com.mauroalexandro.anappstore.network.ApiClient
import com.mauroalexandro.anappstore.network.Resource
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getWrongURL() {
        val infoStatusFail = "fail"
        val errorDesc = "Invalid value \"maybenot\" for argument 'api_list'"
        var infoStatusReceived = ""
        var errorDescReceived = ""

        val apiClient: ApiClient = ApiClient.getInstance()
        apiClient.BASEURL = "http://ws2.aptoide.com/api/6/bulkRequest/api_list/maybenot/"
        apiClient.getClient()?.getApps()?.enqueue(object :
            Callback<AppList> {
            override fun onResponse(
                call: Call<AppList>,
                response: Response<AppList>
            ) {
                val appListResponse: AppList? = response.body()
                if(appListResponse?.info != null &&  appListResponse.errors?.size!! > 0) {
                    infoStatusReceived = appListResponse.info?.status!!
                    errorDescReceived = appListResponse.errors!![0].description

                    assertEquals(infoStatusFail, infoStatusReceived)
                    assertEquals(errorDesc, errorDescReceived)
                }
            }

            override fun onFailure(call: Call<AppList>, t: Throwable) {
                call.cancel()
            }
        })
    }
}