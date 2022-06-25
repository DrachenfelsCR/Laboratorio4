package com.example.laboratorio4

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfesorAlumnos_RecyclerViewAdapter(private var items: ArrayList<DatoAlumnoNotaProfesor>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    var itemList: ArrayList<DatoAlumnoNotaProfesor>? = null
    var alGrupoProfesorDao = DatoAlumnoProfesorDAO.instance

    init {
        val arr =  alGrupoProfesorDao.getAlumnosProfesorDatos()
        this.itemList = arr
    }

    inner class AplicanteHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row_profesoralumnosnota, parent, false)
        return AplicanteHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvProfesorIdAlumno)?.text = "Id: "+item?.IdAlumno
        holder.itemView.findViewById<TextView>(R.id.tvProfesorNombreAlumno)?.text = "Nombre: "+item?.NombreAlumno
        holder.itemView.findViewById<TextView>(R.id.tvProfesorNota)?.text = "Nota: "+item?.Nota.toString()

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
                    val resultList = ArrayList<DatoAlumnoNotaProfesor>()
                    for (row in items) {
                        if (row.NombreAlumno.toLowerCase().contains(charSearch.toLowerCase()) || row.IdAlumno.toLowerCase().contains(charSearch.toLowerCase())) {
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
                itemList = results?.values as ArrayList<DatoAlumnoNotaProfesor>
                notifyDataSetChanged()
            }

        }
    }

}