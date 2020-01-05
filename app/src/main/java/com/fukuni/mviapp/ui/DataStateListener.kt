package com.fukuni.mviapp.ui

import com.fukuni.mviapp.util.DataState

interface DataStateListener {

    fun onDataStateChane(dataState: DataState<*>?)
}