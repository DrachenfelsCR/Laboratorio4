package com.example.laboratorio4

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class Matricular_RecyclerViewAdapter (private var items: ArrayList<AlumnoGrupo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    var itemList: ArrayList<AlumnoGrupo>? = null
    var alumnoGrupoDao = AlumnoGrupoDAO.instance

    init {
        val arr =  alumnoGrupoDao.getAlumnosGrupos()
        this.itemList = arr
    }

    inner class AplicanteHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row_matricular, parent, false)
        return AplicanteHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.itemView.findViewById<TextView>(R.id.tvMatricularCurso)?.text = "Curso: "+item?.CodigoCurso
        holder.itemView.findViewById<TextView>(R.id.tvMatricularId)?.text = "Id: "+item?.Id
        holder.itemView.findViewById<TextView>(R.id.tvMatricularNumGrupo)?.text = "N.Grupo: "+item?.NumeroGrupo.toString()
        holder.itemView.findViewById<TextView>(R.id.tvMatricularIdProfesor)?.text ="Profesor: "+ item?.IdProfesor
        holder.itemView.findViewById<TextView>(R.id.tvMatricularHoraIni)?.text = "Hora Inicio: "+item?.HoraInicio
        holder.itemView.findViewById<TextView>(R.id.tvMatricularHoraFin)?.text = "Hora Fin: "+item?.HoraFin.toString()

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
                    val resultList = ArrayList<AlumnoGrupo>()
                    for (row in items) {
                        if (row.CodigoCurso.toLowerCase().contains(charSearch.toLowerCase()) || row.CodigoCurso.toLowerCase().contains(charSearch.toLowerCase())) {
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
                itemList = results?.values as ArrayList<AlumnoGrupo>
                notifyDataSetChanged()
            }

        }
    }

}