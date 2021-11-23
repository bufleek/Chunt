package com.sports.crichunt.ui.fixture.lineups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Player
import com.sports.crichunt.ui.player.PlayerActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LineUpFragment : Fragment() {
    private val lineup = ArrayList<Player>()
    private val lineupsAdapter by lazy {
        LineupsAdapter {
            startActivity(Intent(requireContext(), PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.KEY_PLAYER, Gson().toJson(it))
            })
        }
    }
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lineup.clear()
        val lineupJson = arguments?.getString(KEY_LINEUP)
        if (lineupJson != null) {
            lineup += Gson().fromJson<ArrayList<Player>>(
                lineupJson,
                object : TypeToken<ArrayList<Player>>() {}.type
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_line_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.recycler_line_up)
        recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lineupsAdapter
        }
        lineupsAdapter.updateData(lineup)
    }

    companion object {
        private const val KEY_LINEUP = "LINEUP"

        @JvmStatic
        fun newInstance(lineup: ArrayList<Player>) = LineUpFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_LINEUP, Gson().toJson(lineup))
            }
        }
    }
}