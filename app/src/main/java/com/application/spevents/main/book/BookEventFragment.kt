package com.application.spevents.main.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.application.spevents.R
import com.application.spevents.dialog.MessageDialog
import com.application.spevents.dialog.ProgressDialog
import com.application.spevents.main.model.BookProfile
import com.application.spevents.main.model.NoNetworkException
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_book_event.*
import javax.inject.Inject

class BookEventFragment : DaggerFragment() {

    @Inject
    lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var viewModel: BookEventViewModel

    @Inject
    lateinit var dialog: MessageDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observeActions()
        bookButton.setOnClickListener {
            val eventId = arguments?.getString("eventId") ?: ""
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()
            viewModel.checkProfileFromBookEvent(eventId, BookProfile(eventId, name, email))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                var bundle: Bundle = bundleOf("checkIn" to false)
                findNavController().navigate(
                    R.id.action_BookEventFragment_to_DetailsFragment,
                    bundle
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeActions() {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            when (it) {
                is BookEventViewState.ShowLoadingState -> {
                    progressDialog.show(requireContext())
                }
                is BookEventViewState.ShowBookEventSucceed -> {
                    progressDialog.hide()
                    dialog.show(
                        context = requireContext(),
                        message = getString(R.string.succeed_check_in)
                    )
                }
                is BookEventViewState.CheckProfileOnEvent -> {
                    if (it.isUserRegistered) {
                        progressDialog.hide()
                        dialog.show(
                            context = requireContext(),
                            message = getString(R.string.error_email_has_registered)
                        )
                    } else {
                        viewModel.requestCheckIn(it.bookProfile)
                    }
                }
                is BookEventViewState.ShowNetworkError -> {
                    progressDialog.hide()
                    Snackbar.make(
                        this.requireView(),
                        it.message,
                        if (it.networkException is NoNetworkException) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}