package com.fukuni.mviapp.api

import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.LiveData
import com.fukuni.mviapp.model.BlogPost
import com.fukuni.mviapp.model.User
import com.fukuni.mviapp.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.BlockingDeque

interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogPosts() : LiveData<GenericApiResponse<List<BlogPost>>>

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId : String
    ): LiveData<GenericApiResponse<User>>
}