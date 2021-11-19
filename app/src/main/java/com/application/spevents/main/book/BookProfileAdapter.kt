package com.application.spevents.main.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.application.spevents.R
import com.application.spevents.model.BookProfile
import kotlinx.android.synthetic.main.book_profile_item.view.*
import java.util.*

class BookProfileAdapter(
    val bookProfiles: List<BookProfile>
) : RecyclerView.Adapter<BookProfileAdapter.ViewHolder>(), Filterable {

    var filteredBookProfiles: List<BookProfile> = bookProfiles

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_profile_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filteredBookProfiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(filteredBookProfiles[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(event: BookProfile) {
            itemView.profileNameTextView.text = event.name
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val ptBR = Locale("pt", "BR")
                val queryString = constraint?.toString()?.toLowerCase(ptBR)
                val filterResults = FilterResults()

                filterResults.values = queryString
                    ?.takeIf { it.isNotBlank() }
                    ?.let { queryWithoutAccents ->
                        bookProfiles.filter {
                            it.name.toLowerCase(ptBR)
                                .contains(queryWithoutAccents) ||
                                    it.name.toLowerCase(ptBR)
                                        .contains(queryWithoutAccents)
                        }
                    } ?: bookProfiles

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBookProfiles = results?.values as List<BookProfile>
                notifyDataSetChanged()
            }
        }
    }
}