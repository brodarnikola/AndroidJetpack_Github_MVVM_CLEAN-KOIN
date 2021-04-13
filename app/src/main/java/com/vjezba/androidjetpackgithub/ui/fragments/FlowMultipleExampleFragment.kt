/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.vjezba.androidjetpackgithub.ui.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.vjezba.androidjetpackgithub.databinding.FragmentFlowMultipleExamplesBinding
import com.vjezba.androidjetpackgithub.ui.adapters.FlowMultipleExamplesAdapter
import com.vjezba.androidjetpackgithub.ui.mapper.LocationViewState
import com.vjezba.androidjetpackgithub.viewmodels.FlowMultipleExamplesViewModel
import com.vjezba.data.Post
import com.vjezba.data.networking.FlowRepositoryApi
import com.vjezba.data.networking.GithubRepositoryApi
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_flow_multiple_examples.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
class FlowMultipleExampleFragment : Fragment() {

    //private val homeViewModel: FlowMultipleExamplesViewModel by viewModel() // by viewModel<FlowWeatherViewModel>()

    private val flowMultipleExamplesAdapter by lazy { FlowMultipleExamplesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFlowMultipleExamplesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.speedDial?.visibility = View.GONE
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initUi()
    }

    private fun initUi() {
        initRecyclerView()
        setupFlowRestApiCall()
        initObservers()
    }

    private fun setupFlowRestApiCall() {

        lifecycleScope.launch {
            val startTime = System.currentTimeMillis() // remember the start time
            (1..3).asFlow().onEach {
                delay(100)
                } // a number every 100 ms
                .flatMapMerge { requestFlow(it) }
                .collect { value -> // collect and print
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }
        }

        lifecycleScope.launch {

            val retrofit = setupRetrofitFlow()

            // 1 donekle dobar primjer, samo steta kaj je sinkroni
//            val postList = retrofit.getPosts()
//            flowMultipleExamplesAdapter.setPosts(postList.toMutableList())
//
//            postList
//                .asFlow()
//                .map {
//                    val dataCommnet =  retrofit.getComments(it.id)
//                    //.onEach {
//                    it.comments = dataCommnet
//                    flowMultipleExamplesAdapter.updatePost(it)
//                    it
//                }
//                .launchIn(this)

            var listPostData = listOf<Post>()

            flowOf( retrofit.getPosts() )
//                .flowOn(Dispatchers.IO)
//                .mapLatest {
//                    listPostData = it
//                    flowMultipleExamplesAdapter.setPosts(it.toMutableList())
//                    it
//                }
//                .flowOn(Dispatchers.Main)
//                .
                .collect {
                    flowMultipleExamplesAdapter.setPosts(it.toMutableList())
                    for( postData in it ) {
                        flowOf( retrofit.getComments(postData.id) )
                            .onEach {
                                postData.comments = it
                                flowMultipleExamplesAdapter.updatePost(postData)
                                it
                            }
                            .launchIn(this)
                    }
                }


//            val test6 = retrofit.getPosts()
//                .asFlow()
//                .map {
//                    val comments = retrofit.getComments(it.id)
//                    it.comments = comments
//                    flowMultipleExamplesAdapter.updatePost(it)
//                    it
//                }
//                .collect {
//                }


//            val test5 = retrofit.getPosts()
//                .asFlow()
//                .debounce(3000)
//                .map {postData ->
//                    val comments = retrofit.getComments(postData.id)
//                    postData.comments = comments
//                    flowMultipleExamplesAdapter.updatePost(postData)
////                        .onEach {
////                            postData.comments = it
////                            flowMultipleExamplesAdapter.updatePost(postData)
////                        }
//                }
//                .launchIn(this)
                //.collect {
                    //flowMultipleExamplesAdapter.setPosts(listPostData.toMutableList())
                //}
//
//            val getPostsData = retrofit.getPosts().asFlow()
////                .collect {
////
////                }
//                .onEach {
//                    println("Data is:  " + it + "\n")
//                }
//                .launchIn(this)



        }

//        getPostObservable()
//            .flatMap { posts ->
//                getCommentsObservable(posts)
//            }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : io.reactivex.Observer<Post> {
//
//
//                override fun onComplete() {}
//
//
//                override fun onSubscribe(d: Disposable) {
//                    githubReposCompositeDisposable?.add(d)
//                }
//
//                override fun onNext(post: Post) {
//                    adapter?.updatePost(post)
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.e(ContentValues.TAG, "onError received: ", e)
//                }
//            })
    }

    private suspend fun setupRetrofitFlow(): FlowRepositoryApi {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(FlowRepositoryApi::class.java)
    }

    private fun initRecyclerView() {

        list_repos_flat_map.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        list_repos_flat_map.adapter = flowMultipleExamplesAdapter
    }

    private fun initObservers() {
//        homeViewModel.forecasts.observe(viewLifecycleOwner, Observer {
//            val locationDetailsFinal = it?.sortedBy { it.date } ?: listOf()
//            //forecastAdapter.setData(locationDetailsFinal.toImmutableList())
//        })
//
//        homeViewModel.locations.observe(viewLifecycleOwner, Observer {
//            //locationAdapter.setData(it)
//        })

        lifecycleScope.launch {
            flowExamples(this)
        }
    }

    private suspend fun flowExamples(coroutineScope: CoroutineScope) {

        combine(f1, f2, f3) { list, list2, list3 ->
            list + list2 + list3
        }
            // print -> [1, 2, 3, 4, 5, 6]
            .onEach {
                println(it)
            }
            .launchIn(coroutineScope)

        (1..3).asFlow().map { requestFlow(it) }
            .onEach { stringData -> println("Data of flow is: " + stringData) }
            .collect()

        // launchIn means, we are collecting data asinkron
        // collect, collectLatest and so on.. means, we are collecting data sinkrono

        exampleOfAsynchronFunction(coroutineScope)// this is asincron function because of operator launchIn
        exampleOfSyncronFunction() // this is sincrono function because of operator collect

        println(
            "Only when this this two above function 'exampleOfAsynchronFunction' and 'exampleOfSyncronFunction'" +
                    "  are done, then only it will be executed this line" +
                    "\n Because this function 'exampleOfSyncronFunction' is suspending function of, because of operator 'collect'  "
        )
    }

    val f1 = flow {
        emit(listOf(1, 2))
    }

    val f2 = flow {
        emit(listOf(3, 4))
    }

    val f3 = flow {
        emit(listOf(5, 6))
    }

    private suspend fun exampleOfSyncronFunction() {
        val nums1 = (1..3).asFlow().onEach { delay(300) }  // numbers 1..4
        val strs1 = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums1.combine(strs1) { a, b -> "$a -> $b" } // compose a single string
            .collect { println("COMBINE operator: Data of two combines flows ( nums and strs ) is: " + it) }
    }

    private fun exampleOfAsynchronFunction(coroutineScope: CoroutineScope) {
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..4
        val strs = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string
            .onEach { println("ZIP operator: Data of two combines flows ( nums and strs ) is: " + it) }
            .launchIn(coroutineScope)
    }

    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }

}
