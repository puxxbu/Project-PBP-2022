package com.example.tubes_pbp.webapi.pesananapi

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes_pbp.databinding.ListDataPesananBinding

class PesananAdapter (
    private val listPesanan:ArrayList<PesananData>,
    private val context: Context

): RecyclerView.Adapter<PesananAdapter.PesananViewHolder>() {
    inner class
    PesananViewHolder(item: ListDataPesananBinding):RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(pesananData: PesananData){
            with(binding) {
                val idData = pesananData.id

                txtNama.text = pesananData.nama
                txtWilayah.text = pesananData.wilayah
                txtAlamat.text = pesananData.alamat
                cvData.setOnClickListener {
                    var i = Intent(context,
                        DetailPesananActivity::class.java).apply {
                        putExtra("id",pesananData.id)
                    }
                    context.startActivity(i)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): PesananViewHolder {
        return PesananViewHolder(ListDataPesananBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }
    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) { holder.bind(listPesanan[position])
    }
    override fun getItemCount(): Int = listPesanan.size
}