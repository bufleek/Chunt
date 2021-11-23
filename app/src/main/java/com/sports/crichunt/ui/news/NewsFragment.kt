package com.sports.crichunt.ui.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelLazy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Feed
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.ui.main.MainViewModel
import com.sports.crichunt.utils.CricHunt
import com.sports.crichunt.utils.MyViewModels
import com.sports.crichunt.utils.ViewModelFactory

class NewsFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var states: FrameLayout
    private val newsAdapter by lazy {
        NewsAdapter {
            val intent = Intent(requireContext(), NewsDetailActivity::class.java)
            intent.putExtra(NewsDetailActivity.KEY_ARTICLE, Gson().toJson(it))
            startActivity(intent)
        }
    }

    private val mainViewModel by ViewModelLazy(
        MainViewModel::class,
        { requireActivity().viewModelStore },
        { ViewModelFactory(MyViewModels.MAIN(((requireActivity().application) as CricHunt).mainRepo)) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_news)
        states = view.findViewById(R.id.states_news)
        recycler.adapter = newsAdapter
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        mainViewModel.newsRequestState.observe(viewLifecycleOwner) {
            states.removeAllViews()
            when (it) {
                is RequestState.Error -> {
                    val errorView = View.inflate(
                        requireContext(),
                        R.layout.item_error,
                        null
                    )
                    errorView.findViewById<TextView>(R.id.tv_error).text = it.error as String
                    errorView.findViewById<Button>(R.id.btn_reload).setOnClickListener {
                        mainViewModel.getNewsArticles()
                    }
                    states.addView(errorView)
                }
                RequestState.Loading -> {
                    states.addView(View.inflate(requireContext(), R.layout.item_loading, null))
                }
                is RequestState.Success<*> -> {
                    if (it.result is Feed) {
                        newsAdapter.updateData(it.result)
                    }
                }
            }
        }
        mainViewModel.getNewsArticles()
    }
}