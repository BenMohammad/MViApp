package com.fukuni.mviapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fukuni.mviapp.model.BlogPost
import com.fukuni.mviapp.model.User
import com.fukuni.mviapp.repository.Repository
import com.fukuni.mviapp.ui.main.state.MainStateEvent
import com.fukuni.mviapp.ui.main.state.MainViewState
import com.fukuni.mviapp.util.AbsentLiveData
import com.fukuni.mviapp.util.DataState

class MainViewModel : ViewModel() {

    private val _stateEvent : MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState : MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState


    val dataState : LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent) {stateEvent ->
            _stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun handleStateEvent(stateEvent: MainStateEvent) : LiveData<DataState<MainViewState>> {
        println("DEBUG: New StateEvent detected: $stateEvent")
        when(stateEvent) {
            is MainStateEvent.GetBlogPostsEvent -> {
                return Repository.getBlogPosts()
            }
            is MainStateEvent.GetUserEvent -> {
                return Repository.getUser(stateEvent.userId)
            }
            is MainStateEvent.None -> {
                return AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUser(user: User) {
        val update =  getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    fun getCurrentViewStateOrNew(): MainViewState {
        val value = viewState.value?.let {
            it
        }?: MainViewState()
        return value

    }

    fun setStateEvent(event: MainStateEvent) {
        val state: MainStateEvent
        state = event
        _stateEvent.value = state
    }

}