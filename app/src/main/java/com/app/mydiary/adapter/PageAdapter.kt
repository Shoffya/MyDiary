package com.app.mydiary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mydiary.R
import com.app.mydiary.model.PageVO
import kotlinx.android.synthetic.main.item_page.view.*

class PageAdapter(
    private val context: Context,
    private val lista: List<PageVO>,
    private val onClick: ((Int) -> Unit)
) : RecyclerView.Adapter<PageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_page,parent,false)
        return PageViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val page = lista[position]
        with(holder.itemView){
            tvTitle.text = page.title
            tvText.text = page.text
            tvDate.text = page.date
            Item.setOnClickListener { onClick(page.id) }
        }
    }

}

class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)