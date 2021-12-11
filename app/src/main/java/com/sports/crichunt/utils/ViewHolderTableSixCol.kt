package com.sports.crichunt.utils

import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R

class ViewHolderTableSixCol(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val table = itemView.findViewById<TableLayout>(R.id.table)

    fun bind(tableData: ArrayList<ArrayList<String>>) {
        table.removeAllViews()
        for ((i, rowData) in tableData.withIndex()) {
            val row = View.inflate(table.context, R.layout.item_table_row_six, null)
            val col0 = row.findViewById<TextView>(R.id.col_0).apply { text = rowData[0] }
            val col1 = row.findViewById<TextView>(R.id.col_1).apply { text = rowData[1] }
            val col2 = row.findViewById<TextView>(R.id.col_2).apply { text = rowData[2] }
            val col3 = row.findViewById<TextView>(R.id.col_3).apply { text = rowData[3] }
            val col4 = row.findViewById<TextView>(R.id.col_4).apply { text = rowData[4] }
            val col5 = row.findViewById<TextView>(R.id.col_5).apply { text = rowData[5] }
            row.findViewById<ImageView>(R.id.img_active_indicator).apply {
                if (rowData.size > 6) if (rowData[6] == "ACTIVE") visibility = View.VISIBLE
            }
            row.findViewById<TextView>(R.id.col_1_1).apply {
                if (rowData.size > 7){
                    if (rowData[7].isBlank()) visibility = View.GONE else{
                        text = rowData[7]
                        visibility = View.VISIBLE
                    }
                }
            }

            if (i == 0) {
                col0.setTextColor(ContextCompat.getColor(table.context, R.color.primary))
                col1.setTextColor(ContextCompat.getColor(table.context, R.color.primary))
                col2.setTextColor(ContextCompat.getColor(table.context, R.color.primary))
                col3.setTextColor(ContextCompat.getColor(table.context, R.color.primary))
                col4.setTextColor(ContextCompat.getColor(table.context, R.color.primary))
                col5.setTextColor(ContextCompat.getColor(table.context, R.color.primary))
            }
            table.addView(row)
        }
    }
}