package com.majestaDev.blitzcalcsession

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tank_info.view.*

class LayoutManager {
    fun getToast(message: String, context: Context) {
        val myToast = Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        )
        myToast.show()
    }

    fun getTankStatisticsView(tankTitle: String, tankAvg: String, tankImageUrl: String, layout: LinearLayout, context: Context): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.tank_info, layout, false)
        view.tank_title.text = tankTitle
        view.tank_avg.text = tankAvg
        Picasso.get().load(tankImageUrl).into(view.tank_image)

        return view
    }
}