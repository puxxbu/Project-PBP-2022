package com.example.tubes_pbp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes_pbp.entity.room.Pesanan
import kotlinx.android.synthetic.main.activity_pesanan_adapter.view.*

class PesananAdapter (private val pesanans: ArrayList<Pesanan>, private val
listener: OnAdapterListener): RecyclerView.Adapter<PesananAdapter.PesananViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananViewHolder {
        return PesananViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_pesanan_adapter,parent,false)
        )
    }

    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) {
        val pesanan = pesanans[position]
        holder.view.text_title.text = pesanan.namaHotel
        holder.view.text_title.setOnClickListener{
            listener.onClick(pesanan)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(pesanan)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(pesanan)
        }
    }

    override fun getItemCount() = pesanans.size
    inner class PesananViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Pesanan>){
        pesanans.clear()
        pesanans.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(pesanan: Pesanan)
        fun onUpdate(pesanan: Pesanan)
        fun onDelete(pesanan: Pesanan)
    }
}