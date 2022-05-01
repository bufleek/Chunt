package com.sports.crichunt.ui.stages.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sports.crichunt.R
import com.sports.crichunt.ui.main.MainActivity

class StagesFragment : Fragment() {
    private lateinit var states: FrameLayout
    private lateinit var recycler: RecyclerView
    private val stagesAdapter by lazy {
//        StagesAdapter {
//            startActivity(Intent(requireContext(), StageActivity::class.java).apply {
//                putExtra(StageActivity.KEY_STAGE, Gson().toJson(it))
//            })
////            requireActivity().finish()
//        }
    }

    private val mainViewModel by lazy {
        (requireActivity() as MainActivity).mainViewModel
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).bottomNav.menu.findItem(R.id.action_stages)
            .isChecked = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        states = view.findViewById(R.id.state_stages)
        recycler = view.findViewById(R.id.recycler_stages)

        recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
//            adapter = stagesAdapter
        }

//        mainViewModel.stagesRequestState.observe(viewLifecycleOwner, {
//            states.removeAllViews()
//            when (it) {
//                is RequestState.Error -> states.addView(
//                    View.inflate(
//                        requireContext(),
//                        R.layout.item_error,
//                        null
//                    ).apply {
//                        findViewById<TextView>(R.id.tv_error).text = it.error
//                    })
//                RequestState.Loading -> states.addView(
//                    View.inflate(
//                        requireContext(),
//                        R.layout.item_loading,
//                        null
//                    )
//                )
//                is RequestState.Success<*> -> {
//                    if (it.result is ArrayList<*>) {
//                        stagesAdapter.updateData(it.result as ArrayList<Stage>)
//                    }
//                    if (stagesAdapter.itemCount == 0) {
//                        states.addView(
//                            View.inflate(
//                                requireContext(),
//                                R.layout.item_empty_state,
//                                null
//                            ).apply {
//                                findViewById<TextView>(R.id.tv_empty_message).text =
//                                    "No series to display"
//                            })
//                    }
//                }
//            }
//        })
//
//        mainViewModel.seasons.observe(viewLifecycleOwner, {
//            stagesAdapter.updateSeasons(it)
//        })
//
//        mainViewModel.getStages()
    }
}