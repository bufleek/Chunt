package com.sports.crichunt.ui.fixture.scoreboard

import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Ball
import com.sports.crichunt.databinding.BindingBalls

class ViewHolderBalls(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val table: TableLayout = itemView.findViewById(R.id.table)

    fun bind(balls: ArrayList<Ball>){
        table.removeAllViews()
        table.addView(View.inflate(table.context, R.layout.item_ball_header, null))
        for (ball in balls){
            table.addView(BindingBalls.inflate(LayoutInflater.from(table.context), null, false).apply {
                this.ball = ball
            }.root)
        }
    }
}