package com.dbtechprojects.cloudstatustest.api

import com.dbtechprojects.cloudstatustest.model.AWSFeed
import retrofit2.Call
import retrofit2.http.GET

interface AwsApiInterface {
    @GET("/rss/all.rss")
    fun getAwsCall(): Call<AWSFeed>
}


