package com.sports.crichunt.ui.player.bowling

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

class PlayerBowlingFragment : Fragment() {
    private lateinit var bowlingTableHead: TableLayout
    private lateinit var bowlingLayoutBody: LinearLayout
    private lateinit var states: FrameLayout

    private val playerViewModel by lazy { (requireActivity() as PlayerActivity).playerViewModel }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_bowling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bowlingTableHead = view.findViewById(R.id.table_bowling_heads)
        bowlingLayoutBody = view.findViewById(R.id.layout_bowling_body)
        states = view.findViewById(R.id.state_player_bowling)

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
                        updateBowling(it.result as ArrayList<Career>)
                    }
                }
                null -> playerViewModel.getCareer()
            }
        })
    }

    private fun updateBowling(career: ArrayList<Career>) {
        bowlingTableHead.removeAllViews()
        bowlingLayoutBody.removeAllViews()
        val tableData = getbBowlingTableData(career)
        if (tableData.isNotEmpty()) {
            for ((i, head) in bowlingHeads.withIndex()) {
                bowlingTableHead.addView(
                    View.inflate(
                        requireContext(),
                        R.layout.item_career_head,
                        null
                    ).apply {
                        findViewById<TextView>(R.id.tv_head).text = head
                    })
            }
            for (columnData in tableData) {
                bowlingLayoutBody.addView(
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
                findViewById<TextView>(R.id.tv_empty_message).text = "No bowling data to display"
            })
        }
    }

    private fun getbBowlingTableData(careerData: ArrayList<Career>): ArrayList<ArrayList<String>> {
        val tableData = ArrayList<ArrayList<String>>()
        val types = arrayListOf<String>()
        for (career in careerData) {
            val bowling = career.bowling
            if (bowling != null) {
                types.add(career.type)
                tableData.add(
                    arrayListOf(
                        career.type,
                        bowling.matches,
                        bowling.overs,
                        bowling.innings,
                        bowling.average,
                        truncateDecimals(bowling.econ_rate),
                        bowling.medians,
                        bowling.runs,
                        bowling.wickets,
                        bowling.wide,
                        bowling.noball,
                        truncateDecimals(bowling.strike_rate),
                        bowling.four_wickets,
                        bowling.five_wickets,
                        bowling.ten_wickets,
                        truncateDecimals(bowling.rate)
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
        private val bowlingHeads = arrayListOf(
            "Bowling",
            "Matches",
            "Overs",
            "Innings",
            "Average",
            "Econ rate",
            "Medians",
            "Runs",
            "Wickets",
            "Wide",
            "No ball",
            "Strike rate",
            "Four wickets",
            "Five wickets",
            "Ten wickets",
            "Rate"
        )
    }
}