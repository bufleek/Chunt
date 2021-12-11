package com.sports.crichunt.ui.stages.fixtures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sports.crichunt.R
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.models.StageFixtures
import com.sports.crichunt.ui.fixture.FixtureActivity
import com.sports.crichunt.ui.main.MainActivity
import com.sports.crichunt.ui.stages.StageActivity

class FixturesFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var states: FrameLayout
    private val fixturesAdapter by lazy {
        FixturesAdapter(
            true,
            {
                startActivity(Intent(requireActivity().baseContext, FixtureActivity::class.java).apply {
                    putExtra(FixtureActivity.FIXTURE, Gson().toJson(it))
                })
            },
            { stage, tab ->
                (requireActivity() as MainActivity).launchStageActivity(stage, tab)
            })
    }

    private val stageViewModel by lazy { (requireActivity() as StageActivity).stageViewModel }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixtures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_stage_fixtures)
        states = view.findViewById(R.id.states_stage_fixtures)
        recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fixturesAdapter
        }

        stageViewModel.stageFixturesRequestState.observe(viewLifecycleOwner, {
            states.removeAllViews()
            when (it) {
                is RequestState.Error -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_error,
                        null
                    ).apply {
                        findViewById<TextView>(R.id.tv_error).text = it.error
                        findViewById<Button>(R.id.btn_reload).setOnClickListener {
                            stageViewModel.getFixtures()
                        }
                    })
                RequestState.Loading -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_loading,
                        null
                    )
                )
                is RequestState.Success<*> -> {
                    if (it.result is StageFixtures) {
                        fixturesAdapter.updateData(it.result.fixtures)
                    }
                    if (fixturesAdapter.itemCount == 0) {
                        states.addView(
                            View.inflate(
                                requireContext(),
                                R.layout.item_empty_state,
                                null
                            ).apply {
                                findViewById<TextView>(R.id.tv_empty_message).text =
                                    "No fixtures to display"
                            })
                    }
                }
            }
        })

        stageViewModel.stage.observe(viewLifecycleOwner, {
            if (it != null) {
                stageViewModel.getFixtures()
            }
        })
    }
}