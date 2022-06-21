package com.example.laboratorio4

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class Cursos_RecyclerViewAdapter (private var items: ArrayList<CursosItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    var itemList: ArrayList<CursosItem>? = null
    var cursosDAO = CursosDAO.instance


    init {
        val arr =  cursosDAO.getCursos()
        this.itemList = arr
    }

    inner class AplicanteHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row_curso, parent, false)
        return AplicanteHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvCodigoCurso)?.text = item?.codigo
        holder.itemView.findViewById<TextView>(R.id.tvNombreCurso)?.text = item?.nombre
        holder.itemView.findViewById<TextView>(R.id.tvCreditosCurso)?.text = "Creditos: " + item?.creditos.toString()
        holder.itemView.findViewById<TextView>(R.id.tvHorasCurso)?.text =  "Horas: " + item?.horasPorSemana.toString()

        holder.itemView.findViewById<ImageButton>(R.id.imgBtnGrupos).setOnClickListener {
            //val i = Intent(holder.itemView.context,CursoActivity::class.java)
           // ContextCompat.startActivity(holder.itemView.context, i, null)
        }
        holder.itemView.findViewById<ImageButton>(R.id.imgButtonBorrarCurso).setOnClickListener {

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
                    val resultList = ArrayList<CursosItem>()
                    for (row in items) {
                        if (row.nombre.toLowerCase().contains(charSearch.toLowerCase()) || row.codigo.toLowerCase().contains(charSearch.toLowerCase())) {
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
                itemList = results?.values as ArrayList<CursosItem>
                notifyDataSetChanged()
            }

        }
    }

}