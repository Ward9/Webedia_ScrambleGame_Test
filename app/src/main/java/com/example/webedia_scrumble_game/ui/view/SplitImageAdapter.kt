package com.example.webedia_scrumble_game.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.webedia_scrumble_game.databinding.ItemGridviewBinding

class SplitImageAdapter(private val context: Context) :
    RecyclerView.Adapter<SplitImageAdapter.ScrambleImgViewHolder>() {

    private var dataList = emptyList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrambleImgViewHolder {
        val binding: ItemGridviewBinding = ItemGridviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrambleImgViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrambleImgViewHolder, position: Int) {
        // Get the data model based on position
        val data = dataList[position]
        // Set bitmap into each imageView
        holder.imageView.setImageBitmap(data)
    }

    //  Total count of items in the list
    override fun getItemCount(): Int = dataList.size

    internal fun setScrambleDataList(dataList: ArrayList<Bitmap>) {
        this.dataList = dataList
        // Allows us to make change on our data list
        val dataListMutable : MutableList<Bitmap> = this.dataList.toMutableList()

        // Get the 4th item from dataList
        val middleItem : Bitmap = this.dataList[4]
        // Drop 4th item from dataListMutable
        dataListMutable.removeAt(4)
        // Then make a shuffle on the rest of the list
        dataListMutable.shuffle()
        // Finally add the middleItem into last position of dataListMutable
        dataListMutable.add(8, middleItem)
        // Set new data into dataList
        this.dataList = dataListMutable
    }

    internal fun setOrderedDataList(dataList: ArrayList<Bitmap>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    internal fun clearDataList() {
        this.dataList = emptyList()
    }

    class ScrambleImgViewHolder(private val itemBinding: ItemGridviewBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var imageView : AppCompatImageView = itemBinding.ivScrumble
    }
}