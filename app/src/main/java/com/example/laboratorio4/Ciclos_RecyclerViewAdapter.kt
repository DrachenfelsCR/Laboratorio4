package com.example.laboratorio4

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class Ciclos_RecyclerViewAdapter (private var items: ArrayList<CiclosItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    var itemList: ArrayList<CiclosItem>? = null
    var ciclosDao = CiclosDAO.instance
    private lateinit var builder: AlertDialog.Builder

    init {
        val arr =  ciclosDao.getCiclos()
        this.itemList = arr
    }

    inner class AplicanteHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row_ciclo, parent, false)
        return AplicanteHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvCicloAnnio)?.text = item?.annio.toString()
        holder.itemView.findViewById<TextView>(R.id.tvCicloNCiclo)?.text = "Ciclo: " + item?.numeroCiclo.toString()
        holder.itemView.findViewById<TextView>(R.id.tvCicloFechaInicio)?.text = "Fecha Inicio: " + item?.fechaInicio
        holder.itemView.findViewById<TextView>(R.id.tvCicloFechaFin)?.text =  "Fecha Fin: " + item?.fechaFin
        holder.itemView.findViewById<TextView>(R.id.tvCicloTitulo)?.text =  item?.titulo
    }

    override fun getItemCount(): Int {
        return itemList?.size!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemList = items
                } else {
                    val resultList = ArrayList<CiclosItem>()
                    for (row in items) {
                        if (row.annio.toString().contains(charSearch.toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    itemList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = itemList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemList = results?.values as ArrayList<CiclosItem>
                notifyDataSetChanged()
            }

        }
    }

}