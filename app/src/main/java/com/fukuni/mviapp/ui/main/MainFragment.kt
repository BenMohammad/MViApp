package com.fukuni.mviapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fukuni.mviapp.R
import com.fukuni.mviapp.model.BlogPost
import com.fukuni.mviapp.model.User
import com.fukuni.mviapp.ui.DataStateListener
import com.fukuni.mviapp.ui.main.state.MainStateEvent
import com.fukuni.mviapp.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException
import java.lang.Exception

class MainFragment : Fragment(), MainRecyclerAdapter.Interaction {

    private val TAG : String = "AppDebug"



    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: CLICKED ${position}")
        println("DEBUG: CLICKED ${item}")
    }

    lateinit var viewModel: MainViewModel
    lateinit var dataStateHandler: DataStateListener
    lateinit var mainRecyclerAdapter : MainRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)


        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        initRecyclerView()
        subscribeObservers()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainFragment.context)
            val topSpacingItemDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecorator)
            mainRecyclerAdapter = MainRecyclerAdapter(this@MainFragment)
            adapter = mainRecyclerAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataStateHandler.onDataStateChane(dataState)
            dataState.data?.let {
                event -> event.getContentIfNotHandled()?.let {
                mainViewState ->
                println("DEBUG: DataState: ${mainViewState}")
                mainViewState.blogPosts?.let {
                    viewModel.setBlogListData(it)
                }

                mainViewState.user?.let {
                    viewModel.setUser(it)
                }

            }
        }
    })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let {blogPosts ->
                println("DEBUG: Setting blogPosts to recyclerView: ${blogPosts}")
                mainRecyclerAdapter.submitList(blogPosts)
            }

            viewState.user?.let {user ->
                println("DEBUG: Setting user data: ${user}")
                setUserProperties(user)
            }
        })

    }

    fun setUserProperties(user: User) {
        email.setText(user.email)
        username.setText(user.username)
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
    }

    fun triggerGetUserEvent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }
    fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostsEvent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_get_blogs -> triggerGetBlogsEvent()

            R.id.action_get_user -> triggerGetUserEvent()
        }


        return super.onOptionsItemSelected(item)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener

        }catch (e: ClassCastException) {
            println("$context must implement DataStateListener")
        }
    }

}