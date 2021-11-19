package com.application.spevents.main.book

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.application.spevents.R
import com.application.spevents.model.BookProfile
import kotlinx.android.synthetic.main.fragment_book_profiles.*

class BookProfilesFragment : Fragment() {

    lateinit var bookProfiles: List<BookProfile>

    lateinit var bookProfileAdapter: BookProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_profiles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        bookProfiles = arguments?.getParcelableArrayList("profiles") ?: emptyList()
        bookProfileAdapter = BookProfileAdapter(bookProfiles)
        profilesRecyclerView.adapter = bookProfileAdapter
        setupSearchList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack(R.id.BookEventFragment, true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearchList() {
        val et = searchView.findViewById(R.id.search_src_text) as TextView
        et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(50))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(textSearch: String): Boolean {
                if (::bookProfiles.isInitialized) {
                    bookProfileAdapter.filter.filter(textSearch)
                }
                return false
            }
        })
    }
}