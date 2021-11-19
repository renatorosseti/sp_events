package com.application.spevents.main.book

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.application.spevents.R
import com.application.spevents.data.Cache.eventDetail
import com.application.spevents.dialog.MessageDialog
import com.application.spevents.dialog.ProgressDialog
import com.application.spevents.model.BookProfile
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_book_event.*
import kotlinx.android.synthetic.main.fragment_book_event.bookButton
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
        setUIView()
    }

    private fun setUIView() {
        showSoftKeyboard()
        nameEditText.requestFocus()
        bookButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()
            viewModel.verifyProfileFromBookEvent(eventDetail.id, BookProfile(eventDetail.id, name, email))
        }
        showBookProfilesButton.setOnClickListener {
            viewModel.fetchProfilesFromBookEvent(eventId = eventDetail.id)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                hideSoftKeyboard(nameEditText.windowToken)
                findNavController().navigate(R.id.EventDetailFragment)
                true
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
                        viewModel.requestBookEvent(it.bookProfile)
                    }
                }
                is BookEventViewState.ShowBookEventProfiles -> {
                    progressDialog.hide()
                    hideSoftKeyboard(nameEditText.windowToken)
                    findNavController().navigate(R.id.action_BookEventFragment_to_bookProfilesFragment,
                        bundleOf("profiles" to it.profiles))
                }
                is BookEventViewState.ShowErrorEmptyData -> {
                    progressDialog.hide()
                    dialog.show(
                        context = requireContext(),
                        message = getString(R.string.error_empty_data)
                    )
                }
                is BookEventViewState.ShowNetworkError -> {
                    progressDialog.hide()
                    dialog.show(
                        context = requireContext(),
                        message = getString(it.message)
                    )
                }
            }
        })
    }

    private fun showSoftKeyboard() =
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
        )

    private fun hideSoftKeyboard(windowToken: IBinder) =
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            windowToken,
            0
        )
}