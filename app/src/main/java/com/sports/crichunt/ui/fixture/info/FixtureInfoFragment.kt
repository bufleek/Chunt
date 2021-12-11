package com.sports.crichunt.ui.fixture.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sports.crichunt.R
import com.sports.crichunt.data.models.FixtureInfo
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.models.Stage
import com.sports.crichunt.ui.fixture.FixtureActivity
import com.sports.crichunt.utils.getDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FixtureInfoFragment : Fragment() {
    private var refreshJob: Job? = null
    private lateinit var layoutStage: View
    private lateinit var tvStage: TextView
    private lateinit var tableInfo: TableLayout
    private lateinit var states: FrameLayout
    private lateinit var tvNote: TextView
    private lateinit var frameAdditionalInfo: FrameLayout
    private var stage: Stage? = null

    private val fixtureViewModel by lazy {
        (requireActivity() as FixtureActivity).fixtureViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixture_info, container, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as FixtureActivity).appBarLayout.setExpanded(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutStage = view.findViewById(R.id.layout_stage)
        tvStage = view.findViewById(R.id.tv_stage)
        tableInfo = view.findViewById(R.id.table_fixture_info)
        states = view.findViewById(R.id.state_fragment_fixture_info)
        tvNote = view.findViewById(R.id.tv_note)
        frameAdditionalInfo = view.findViewById(R.id.layout_additional_info)

        layoutStage.setOnClickListener {
            (requireActivity() as FixtureActivity).showInterstitialAd(
                FixtureActivity.LAUNCH_STAGE,
                stage
            )
//            requireActivity().finish()
        }

        fixtureViewModel.fixtureInfoRequestState.observe(viewLifecycleOwner) {
            states.removeAllViews()
            frameAdditionalInfo.removeAllViews()
            when (it) {
                is RequestState.Error -> if (tableInfo.childCount == 0) {
                    states.addView(
                        View.inflate(
                            requireContext(),
                            R.layout.item_error,
                            null
                        ).apply {
                            findViewById<TextView>(R.id.tv_error).text = it.error
                            findViewById<Button>(R.id.btn_reload).setOnClickListener { fixtureViewModel.getFixtureInfo() }
                        })
                } else refreshInfo()

                RequestState.Loading -> if (tableInfo.childCount == 0) { states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_loading,
                        null
                    )
                )
                }
                is RequestState.Success<*> -> {
                    if (it.result is FixtureInfo) {
                        updateFixtureInfo(it.result)
                    }
                    refreshInfo()
                }
            }
        }

        fixtureViewModel.getFixtureInfo()
    }

    private fun updateFixtureInfo(fixtureInfo: FixtureInfo) {
        (requireActivity() as FixtureActivity).updateScore(
            fixtureInfo.scoreboards,
            fixtureInfo.localteam,
            fixtureInfo.visitorteam
        )
        tableInfo.removeAllViews()
        frameAdditionalInfo.removeAllViews()
        tvStage.text = fixtureInfo.stage.name
        fixtureViewModel.fixture.value?.let {
            if (it.note.isBlank()) {
                getDate(tvNote, it.starting_at)
                tvNote.text = "Starts at ${tvNote.text}"
            } else {
                tvNote.text = it.note
            }
        }
        stage = fixtureInfo.stage
        val tableData = getTableData(fixtureInfo)
        tableData.forEach { data ->
            tableInfo.addView(
                View.inflate(requireContext(), R.layout.item_table_row_two, null).apply {
                    findViewById<TextView>(R.id.tv_col_0).text = data[0]
                    val col2 = findViewById<TextView>(R.id.tv_col_1)
                    if (data[0] == "Date:") {
                        getDate(col2, data[1])
                    } else {
                        col2.text = data[1]
                    }
                })
        }
    }

    private fun getTableData(fixtureInfo: FixtureInfo): ArrayList<ArrayList<String>> {
        val tableData = ArrayList<ArrayList<String>>()
        val fixture = fixtureViewModel.fixture.value
        if (fixture != null) {
            tableData.add(arrayListOf("Status:", fixture.status))
            tableData.add(arrayListOf("Match type:", fixture.type))
            fixture.round?.let { tableData.add(arrayListOf("Round:", it)) }
            tableData.add(arrayListOf("Date:", fixture.starting_at))
        }
        tableData.add(arrayListOf("Venue:", fixtureInfo.venue?.name ?: "No data"))
        tableData.add(arrayListOf("Referee:", fixtureInfo.referee?.fullname ?: "No data"))
        var umpires = ""
        fixtureInfo.firstumpire?.let { umpires += it.fullname }
        fixtureInfo.secondumpire?.let { umpires += ", ${it.fullname}" }
        fixtureInfo.tvumpire?.let { umpires += ", ${it.fullname}" }
        tableData.add(arrayListOf("Umpires:", if (umpires.isNotBlank()) umpires else "No data"))
        return tableData
    }

    private fun refreshInfo() {
        val fixture = fixtureViewModel.fixture.value!!
        if (fixture.live && !fixture.status.equals(
                "Finished",
                true
            ) && !fixture.status.equals("Aban.", true)
        ) {
            refreshJob?.cancel()
            refreshJob = lifecycleScope.launch {
                delay(REFRESH_INTERVAL)
                fixtureViewModel.getFixtureInfo()
            }
            refreshJob?.start()
        }
    }

    companion object {
        private const val REFRESH_INTERVAL = 20000L
    }
}