package com.sports.crichunt.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.sports.crichunt.R
import com.sports.crichunt.ui.main.MainActivity

class MoreFragment : Fragment() {
    private lateinit var layoutShare: LinearLayout
    private lateinit var layoutRate: LinearLayout
    private lateinit var layoutPrivacy: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).bottomNav.menu.findItem(R.id.action_more)
            .isChecked = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutShare = view.findViewById(R.id.layout_share)
        layoutRate = view.findViewById(R.id.layout_rate)
        layoutPrivacy = view.findViewById(R.id.layout_privacy)

        layoutRate.setOnClickListener { rateApp() }
        layoutShare.setOnClickListener { shareApp() }
    }

    private fun rateApp() {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${requireActivity().packageName}")
            )
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=${requireActivity().packageName}")
            )
            startActivity(intent)
        }
    }

    private fun shareApp() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                Uri.parse("http://play.google.com/store/apps/details?id=${requireActivity().packageName}")
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

}