package com.fukuni.mviapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.fukuni.mviapp.R
import com.fukuni.mviapp.ui.DataStateListener
import com.fukuni.mviapp.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DataStateListener {




    override fun onDataStateChane(dataState: DataState<*>?) {
        handleDataStateChange(dataState)
    }

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showMainFragment()
    }

    fun showMainFragment() {
        if(supportFragmentManager.fragments.size == 0) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    MainFragment(),
                    "MainFragment"
                )
                .commit()
        }
    }

    fun handleDataStateChange(dataState: DataState<*>?) {
        dataState?.let {
            showProgressBar(dataState.loading)
            dataState.message?.let {event ->
                event.getContentIfNotHandled()?.let {
                    message -> showToast(message)
                }
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showProgressBar(isVisible: Boolean) {
        if(isVisible) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.INVISIBLE
        }
    }
}
