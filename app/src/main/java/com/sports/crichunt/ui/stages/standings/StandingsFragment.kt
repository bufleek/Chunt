package com.sports.crichunt.ui.stages.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sports.crichunt.R
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.data.models.Standing
import com.sports.crichunt.ui.stages.StageActivity

class StandingsFragment : Fragment() {
    private lateinit var tableStandings: TableLayout
    private lateinit var states: FrameLayout

    private val stageViewModel by lazy { (requireActivity() as StageActivity).stageViewModel }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_standings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        states = view.findViewById(R.id.state_standings)
        tableStandings = view.findViewById(R.id.table_standings)

        stageViewModel.stage.observe(viewLifecycleOwner, {
            if (it == null || !it.standings) {
                states.addView(
                    View.inflate(requireContext(), R.layout.item_empty_state, null).apply {
                        findViewById<TextView>(R.id.tv_empty_message).text =
                            "No standings available for this series"
                    })
            } else {
                stageViewModel.getStandings()
            }
        })

        stageViewModel.standingsRequestState.observe(viewLifecycleOwner, {
            states.removeAllViews()
            when (it) {
                is RequestState.Error -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_error,
                        null
                    ).apply {
                        findViewById<TextView>(R.id.tv_error).text = it.error
                        findViewById<Button>(R.id.btn_reload).setOnClickListener { stageViewModel.getStandings() }
                    })
                RequestState.Loading -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_loading,
                        null
                    )
                )
                is RequestState.Success<*> -> {
                    if (it.result is ArrayList<*>) {
                        updateStandings(it.result as ArrayList<Standing>)
                    }
                }
            }
        })
    }

    private fun updateStandings(standings: java.util.ArrayList<Standing>) {
        tableStandings.removeAllViews()
        val tableData = getTableData(standings)
        if (tableData.isEmpty()){
            states.addView(View.inflate(requireContext(), R.layout.item_empty_state, null).apply {
                findViewById<TextView>(R.id.tv_empty_message).text = "No standings available for this series"
            })
        }else{
            for ((i, rowData) in tableData.withIndex()) {
                val row = View.inflate(requireContext(), R.layout.item_standing, null)
                val rank = row.findViewById<TextView>(R.id.tv_rank).apply { text = rowData[0] }
                val team = row.findViewById<TextView>(R.id.tv_team).apply { text = rowData[1] }
                val played = row.findViewById<TextView>(R.id.tv_played).apply { text = rowData[2] }
                val won = row.findViewById<TextView>(R.id.tv_win).apply { text = rowData[3] }
                val lost = row.findViewById<TextView>(R.id.tv_loss).apply { text = rowData[4] }
                val draw = row.findViewById<TextView>(R.id.tv_draw).apply { text = rowData[5] }
                val noResults =
                    row.findViewById<TextView>(R.id.tv_no_result).apply { text = rowData[6] }
                val points = row.findViewById<TextView>(R.id.tv_points).apply { text = rowData[7] }
                if (i == 0) {
                    rank.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    team.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    played.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    won.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    lost.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    draw.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    noResults.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    points.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                }
                tableStandings.addView(row)
            }
        }
    }

    private fun getTableData(standings: ArrayList<Standing>): ArrayList<ArrayList<String>> {
        val tableData = ArrayList<ArrayList<String>>()
        for (standing in standings) {
            tableData.add(
                arrayListOf(
                    standing.position,
                    standing.team.name,
                    standing.played,
                    standing.won,
                    standing.lost,
                    standing.draw,
                    standing.noresult,
                    standing.points
                )
            )
        }
        if (tableData.isNotEmpty()) {
            tableData.add(0, arrayListOf("-", "Team", "P", "W", "L", "D", "NR", "P"))
        }
        return tableData
    }
}