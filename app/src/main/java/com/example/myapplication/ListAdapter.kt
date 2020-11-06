package com.example.myapplication

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import name.ank.lab4.BibEntry
import name.ank.lab4.Types

class ListAdapter(private val dataSet: ArrayList<BibEntry>) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: View) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false) as View
        return MyViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].type.ordinal
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataEntry = dataSet[position]
        val formattedData = getFormattedData(dataEntry)
        val view = holder.cardView.rootView

        val iconSrc = when (dataEntry.type) {
            Types.ARTICLE -> R.drawable.article_icon
            Types.BOOK -> R.drawable.book_icon
            Types.INCOLLECTION -> R.drawable.incollection_icon
            Types.INPROCEEDINGS -> R.drawable.inproceedings_icon
            Types.MISC -> R.drawable.misc_icon
            Types.TECHREPORT -> R.drawable.techreport_icon
            Types.UNPUBLISHED -> R.drawable.unpublished_icon
            else -> R.drawable.default_icon
        }
        view.findViewById<ImageView>(R.id.icon).setImageResource(iconSrc)

        val borderColour = when (formattedData.displayType) {
            DisplayType.FRAGMENT -> R.color.blueBorder
            DisplayType.INDEPENDENT -> R.color.greenBorder
            DisplayType.UNPUBLISHED -> R.color.redBorder
            DisplayType.DEFAULT -> R.color.defaultBorder
        }
        view.findViewById<FrameLayout>(R.id.card_border).setBackgroundResource(borderColour)

        view.findViewById<TextView>(R.id.title).text = formattedData.title
        view.findViewById<TextView>(R.id.authors).text = formattedData.authors
        view.findViewById<TextView>(R.id.publish_body).text = formattedData.publishInfo

        val infoContainer = view.findViewById<LinearLayout>(R.id.info_container)

        val sourceContainer = infoContainer.findViewById<LinearLayout>(R.id.source)
        if (formattedData.source != null && sourceContainer != null) {
            sourceContainer.findViewById<TextView>(R.id.source_body).text = formattedData.source
        } else {
            infoContainer.removeView(sourceContainer)
        }

        val pagesContainer = infoContainer.findViewById<LinearLayout>(R.id.pages)
        if (formattedData.pages != null && pagesContainer != null) {
            pagesContainer.findViewById<TextView>(R.id.pages_body).text = formattedData.pages
        } else {
            infoContainer.removeView(pagesContainer)
        }

        val urlContainer = infoContainer.findViewById<LinearLayout>(R.id.url)
        if (formattedData.url != null && urlContainer != null) {
            val infoBody = urlContainer.findViewById<TextView>(R.id.url_body)
            infoBody.text = formattedData.url
            infoBody.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        } else {
            infoContainer.removeView(urlContainer)
        }
    }

}