package com.fukuni.mviapp.ui.main.state

import com.fukuni.mviapp.model.BlogPost
import com.fukuni.mviapp.model.User

data class MainViewState (

    var blogPosts : List<BlogPost>? = null,

    var user : User? = null
)