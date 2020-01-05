package com.fukuni.mviapp.repository

import androidx.lifecycle.LiveData
import com.fukuni.mviapp.api.MyRetrofitBuilder
import com.fukuni.mviapp.model.BlogPost
import com.fukuni.mviapp.model.User
import com.fukuni.mviapp.ui.main.state.MainViewState
import com.fukuni.mviapp.util.ApiSuccessResponse
import com.fukuni.mviapp.util.DataState
import com.fukuni.mviapp.util.GenericApiResponse

object Repository {

    fun getBlogPosts() : LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<List<BlogPost>, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(
                    null,
                    MainViewState(
                        blogPosts = response.body,
                        user = null
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return MyRetrofitBuilder.apiService.getBlogPosts()
            }

        }.asLiveData()

    }

    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<User, MainViewState>() {

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    null,
                    MainViewState(
                        blogPosts = null,
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return MyRetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()
    }



}