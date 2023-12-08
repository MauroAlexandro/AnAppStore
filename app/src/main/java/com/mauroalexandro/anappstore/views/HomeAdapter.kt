package com.mauroalexandro.anappstore.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mauroalexandro.anappstore.R
import com.mauroalexandro.anappstore.models.App

class HomeAdapter(
    private val context: Context
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var appList = arrayListOf<App>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.app_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //App
        val app: App = getAppList(position)

        //App Name
        viewHolder.appName.text = app.name

        //App Image
        val appImage = app.icon
        Glide
            .with(context)
            .load(appImage)
            .centerCrop()
            .apply(RequestOptions().override(500, 500))
            .into(viewHolder.appImage)
    }

    override fun getItemCount(): Int {
        return appList.size
    }
    private fun getItemPosition(app: App): Int {
        return appList.indexOfFirst { it.id == app.id }
    }

    private fun getAppList(position: Int): App {
        return appList[position]
    }

    fun getItemAtPosition(position: Int): App {
        return appList[position]
    }

    fun setAppList(appList: List<App>) {
        if(this.appList.isEmpty())
            this.appList = appList as ArrayList<App>
        else
            this.appList.addAll(appList)
        notifyDataSetChanged()
    }

    fun updateItem(app: App) {
        val itemPosition = getItemPosition(app)
        notifyItemChanged(itemPosition)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.app_name_cell)
        val appImage: ImageView = view.findViewById(R.id.app_image_cell)
    }
}