package com.sports.crichunt.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sports.crichunt.R
import com.sports.crichunt.data.models.Venue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("score")
fun score(textView: TextView, score: String? = null) {
    score?.let {
        textView.text = it
    }
}

@BindingAdapter("over")
fun over(textView: TextView, over: String? = null) {
    over?.let {
        textView.text = "$it ov"
    }
}

@BindingAdapter("fixtureImage")
fun fixtureImage(imageView: ImageView, url: String?) {
    Glide.with(imageView).load(url).placeholder(R.drawable.ic_baseline_sports_cricket_24).into(imageView)
}

@BindingAdapter("type", "round")
fun typeRound(textView: TextView, type: String?, round: String?) {
    var text = ""
    if (round != null && round.isNotBlank()) {
        text = round
    } else if (type != null && type.isNotBlank()) {
        text = type
    } else {
        textView.visibility = View.GONE
    }
    textView.text = text
}

@BindingAdapter("date")
fun date(textView: TextView, date: String?) {
    date?.let {
        getDate(textView, it)
    }
}

@BindingAdapter("playerImage")
fun playerImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url).into(imageView)
}

@BindingAdapter("runs")
fun runs(textView: TextView, runs: String?) {
    textView.text = "R:${runs ?: 0}"
}

@BindingAdapter("bye")
fun bye(textView: TextView, bye: String?) {
    textView.text = "B:${bye ?: 0}"
}

@BindingAdapter("legbye")
fun legbye(textView: TextView, legbye: String?) {
    textView.text = "LB:${legbye ?: 0}"
}

@BindingAdapter("noball")
fun noball(textView: TextView, noball: String?) {
    textView.text = "NB:${noball ?: 0}"
}

@BindingAdapter("noballruns")
fun noballruns(textView: TextView, noballruns: String?) {
    textView.text = "NBR:${noballruns ?: 0}"
}

@BindingAdapter("wickets")
fun wickets(textView: TextView, wickets: String?) {
    textView.text = "W:${wickets ?: 0}"
}

@BindingAdapter("penalty")
fun penalty(textView: TextView, penalty: String?) {
    textView.text = "P:${penalty ?: 0}"
}

@BindingAdapter("total")
fun total(textView: TextView, total: String?) {
    textView.text = "T:${total ?: 0}"
}

@BindingAdapter("venue")
fun venue(textView: TextView, venue: Venue?){
    if (venue == null){
        textView.visibility = View.GONE
        return
    }
    textView.text = venue.name
}

@BindingAdapter("articleImage")
fun articleImage(imageView: ImageView, image: String?){
    Glide.with(imageView).load(image).into(imageView)
}

@BindingAdapter("publishDate")
fun publishDate(textView: TextView, date: String?){
    if (date != null) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sszzzz", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val formattedDate = dateFormat.parse(date)
            textView.text =
                SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(formattedDate)
        } catch (e: ParseException) {
            textView.text = e.message
        }
    }
}