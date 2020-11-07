package com.example.myapplication

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import name.ank.lab4.BibDatabase
import name.ank.lab4.BibEntry
import name.ank.lab4.Types

class ListAdapter(
    private val dataSet: ArrayList<BibEntry>,
    recyclerView: RecyclerView,
    database: BibDatabase
) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = (recyclerView.layoutManager!!) as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisiblePosition + 1 >= layoutManager.itemCount) {
                    recyclerView.post {
                        for (i in 0 until database.size()) {
                            dataSet.add(database.getEntry(i))
                        }
                        notifyDataSetChanged()
                    }
                }
            }
        })
    }

    class MyViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
        var icon: ImageView? = null
        var border: FrameLayout? = null

        var title: TextView? = null
        var authors: TextView? = null
        var publish: TextView? = null

        var infoContainer: LinearLayout? = null

        var sourceContainer: LinearLayout? = null
        var pagesContainer: LinearLayout? = null
        var urlContainer: LinearLayout? = null

        var sourceBody: TextView? = null
        var pagesBody: TextView? = null
        var urlBody: TextView? = null

        init {
            icon = cardView.findViewById(R.id.icon)
            border = cardView.findViewById(R.id.card_border)

            title = cardView.findViewById(R.id.title)
            authors = cardView.findViewById(R.id.authors)
            publish = cardView.findViewById(R.id.publish_body)

            infoContainer = cardView.findViewById(R.id.info_container)

            sourceContainer = infoContainer?.findViewById(R.id.source)
            pagesContainer = infoContainer?.findViewById(R.id.pages)
            urlContainer = infoContainer?.findViewById(R.id.url)

            sourceBody = sourceContainer?.findViewById(R.id.source_body)
            pagesBody = pagesContainer?.findViewById(R.id.pages_body)
            urlBody = urlContainer?.findViewById(R.id.url_body)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false) as View
        return MyViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataEntry = dataSet[position]
        val formattedData = getFormattedData(dataEntry)

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
        holder.icon?.setImageResource(iconSrc)

        val borderColour = when (formattedData.displayType) {
            DisplayType.FRAGMENT -> R.color.blueBorder
            DisplayType.INDEPENDENT -> R.color.greenBorder
            DisplayType.UNPUBLISHED -> R.color.redBorder
            DisplayType.DEFAULT -> R.color.defaultBorder
        }
        holder.border?.setBackgroundResource(borderColour)

        holder.title?.text = formattedData.title
        holder.authors?.text = formattedData.authors
        holder.publish?.text = formattedData.publishInfo

        if (formattedData.source != null) {
            holder.sourceBody?.text = formattedData.source
        } else {
            holder.infoContainer?.removeView(holder.sourceContainer)
        }

        if (formattedData.pages != null) {
            holder.pagesBody?.text = formattedData.pages
        } else {
            holder.infoContainer?.removeView(holder.pagesContainer)
        }

        if (formattedData.url != null) {
            holder.urlBody?.text = formattedData.url
            holder.urlBody?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        } else {
            holder.infoContainer?.removeView(holder.urlContainer)
        }
    }

}