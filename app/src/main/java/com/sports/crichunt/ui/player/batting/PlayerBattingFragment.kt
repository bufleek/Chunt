package com.sports.crichunt.ui.player.batting

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Career
import com.sports.crichunt.data.models.RequestState
import com.sports.crichunt.ui.player.PlayerActivity
import java.lang.Exception

class PlayerBattingFragment : Fragment() {
    private lateinit var battingTableHead: TableLayout
    private lateinit var battingLayoutBody: LinearLayout
    private lateinit var states: FrameLayout

    private val playerViewModel by lazy { (requireActivity() as PlayerActivity).playerViewModel }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_batting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        battingTableHead = view.findViewById(R.id.table_batting_heads)
        battingLayoutBody = view.findViewById(R.id.layout_batting_body)
        states = view.findViewById(R.id.state_player_batting)

        playerViewModel.careerRequestState.observe(viewLifecycleOwner, {
            states.removeAllViews()
            when (it) {
                is RequestState.Error -> states.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_error,
                        null
                    ).apply {
                        findViewById<TextView>(R.id.tv_error).text = it.error
                        findViewById<Button>(R.id.btn_reload).setOnClickListener { playerViewModel.getCareer() }
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
                        updateBatting(it.result as ArrayList<Career>)
                    }
                }
                null -> playerViewModel.getCareer()
            }
        })
    }

    private fun updateBatting(career: ArrayList<Career>) {
        battingTableHead.removeAllViews()
        battingLayoutBody.removeAllViews()
        val tableData = getBattingTableData(career)
        if (tableData.isNotEmpty()) {
            for ((i, head) in battingHeads.withIndex()) {
                battingTableHead.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_career_head,
                        null
                    ).apply {
                        findViewById<TextView>(R.id.tv_head).text = head
                    })
            }
            for (columnData in tableData) {
                battingLayoutBody.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_career_body,
                        null
                    ).apply {
                        val tBody = findViewById<TableLayout>(R.id.table_career_body)
                        for ((i, rowData) in columnData.withIndex()) {
                            tBody.addView(
                                View.inflate(
                                    requireContext(),
                                    R.layout.item_career_body_text,
                                    null
                                ).apply {
                                    findViewById<TextView>(R.id.tv_career_text).apply {
                                        text = if (i != 0) rowData else {
                                            setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.primary
                                                )
                                            )
                                            SpannableStringBuilder().bold {
                                                append(rowData)
                                            }
                                        }
                                    }
                                })
                        }
                    })
            }
        }else{
            states.addView(View.inflate(requireContext(), R.layout.item_empty_state, null).apply {
                findViewById<TextView>(R.id.tv_empty_message).text = "No batting data to display"
            })
        }
    }

    private fun getBattingTableData(careerData: ArrayList<Career>): ArrayList<ArrayList<String>> {
        val tableData = ArrayList<ArrayList<String>>()
        val types = arrayListOf<String>()
        for (career in careerData) {
            val batting = career.batting
            if (batting != null) {
                types.add(career.type)
                tableData.add(
                    arrayListOf(
                        career.type,
                        batting.matches,
                        batting.innings,
                        batting.runs_scored,
                        batting.not_outs,
                        batting.highest_inning_score,
                        truncateDecimals(batting.strike_rate),
                        batting.balls_faced,
                        batting.average,
                        batting.four_x,
                        batting.six_x,
                        truncateDecimals(batting.fow_score),
                        truncateDecimals(batting.fow_balls),
                        batting.hundreds,
                        batting.fifties
                    )
                )
            }
        }
        return tableData
    }

    companion object {
        private fun truncateDecimals(number: String): String{
            try {
                if (number.contains(".")){
                    return String.format("%.2f", number.toFloatOrNull())
                }else{
                    return number
                }
            }catch(e: Exception) {
                return number
            }
        }
        private val battingHeads = arrayListOf(
            "Batting",
            "Matches",
            "Innings",
            "Runs scored",
            "Not outs",
            "Highest inning",
            "Strike rate",
            "Balls faced",
            "Average",
            "Fours",
            "Sixes",
            "Fow score",
            "Fow balls",
            "Hundreds",
            "Fifties"
        )
    }
}