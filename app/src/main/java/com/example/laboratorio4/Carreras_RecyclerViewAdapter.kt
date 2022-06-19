package com.example.laboratorio4

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class Carreras_RecyclerViewAdapter (private var items: ArrayList<CarrerasItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var itemList: ArrayList<CarrerasItem>? = null
    var carrerasDao = CarrerasDAO.instance
    var oracleDao = OracleDAO.instance


    init {
        oracleDao.loadAllCarreras()
        val arr =  carrerasDao.getCarreras()
        this.itemList = arr
    }

    inner class AplicanteHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row_carrera, parent, false)
        return AplicanteHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvCodigoCarrera)?.text = item?.codigo
        holder.itemView.findViewById<TextView>(R.id.tvNombreCurso)?.text = item?.nombre
        holder.itemView.findViewById<TextView>(R.id.tvCreditosCurso)?.text = item?.titulo

        holder.itemView.findViewById<Button>(R.id.btnCursosGrupos).setOnClickListener {
           val i = Intent(holder.itemView.context,CursoActivity::class.java)
            startActivity(holder.itemView.context,i,null)
        }
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
                    val resultList = ArrayList<CarrerasItem>()
                    for (row in items) {
                        if (row.nombre.toLowerCase().contains(charSearch.toLowerCase()) || row.titulo.toLowerCase().contains(charSearch.toLowerCase())
                            || row.codigo.toLowerCase().contains(charSearch.toLowerCase())) {
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
                itemList = results?.values as ArrayList<CarrerasItem>
                notifyDataSetChanged()
            }

        }
    }

}