package com.sports.crichunt.ui.player.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.ui.player.PlayerActivity

class PlayerInfoFragment : Fragment() {
    private lateinit var tablePlayerInfo: TableLayout

    private val playerViewModel by lazy { (requireActivity() as PlayerActivity).playerViewModel }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tablePlayerInfo = view.findViewById(R.id.table_player_info)

        playerViewModel.player.observe(viewLifecycleOwner, {
            it?.let { player -> updateInfo(player) }
        })
    }

    private fun updateInfo(player: Player) {
        tablePlayerInfo.removeAllViews()
        val tableData = getInfoTableData(player)
        for (rowData in tableData) {
            val row = View.inflate(requireContext(), R.layout.item_table_row_two, null)
            row.findViewById<TextView>(R.id.tv_col_0).text = rowData[0]
            row.findViewById<TextView>(R.id.tv_col_1).text = rowData[1]
            tablePlayerInfo.addView(row)
        }
    }

    private fun getInfoTableData(player: Player): ArrayList<ArrayList<String>> {
        val tableData = ArrayList<ArrayList<String>>()
        tableData.add(arrayListOf("Full name:", player.fullname))
        tableData.add(arrayListOf("Gender:", player.gender))
        tableData.add(arrayListOf("Batting style:", player.battingstyle ?: "No data"))
        tableData.add(arrayListOf("Bowling style:", player.bowlingstyle ?: "No data"))
        tableData.add(arrayListOf("Position:", player.position.name))
        return tableData
    }
}