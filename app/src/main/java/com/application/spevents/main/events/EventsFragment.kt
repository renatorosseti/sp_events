package com.application.spevents.main.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.application.spevents.R
import com.application.spevents.data.Cache.eventDetail
import com.application.spevents.dialog.ProgressDialog
import com.application.spevents.model.Event
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_events.*
import javax.inject.Inject

class EventsFragment : DaggerFragment() {

    @Inject
    lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var viewModel: EventsViewModel

    override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchEvents()
    }

    private fun observeActions() {
        viewModel.response.observe(viewLifecycleOwner, Observer {
          when (it) {
            is EventsViewState.ShowLoadingState -> {
              progressDialog.show(requireContext())
            }
            is EventsViewState.ShowContentFeed -> {
              progressDialog.hide()
              updateAdapter(it.events)
            }
            is EventsViewState.ShowNetworkError -> {
              progressDialog.hide()
              Snackbar.make(
                  this.requireView(),
                it.message,
                Snackbar.LENGTH_LONG
              ).show()
            }
          }
        })
    }

    private fun updateAdapter(events: List<Event>) {
        val mainAdapter = EventsAdapter(events)
        recyclerView.adapter = mainAdapter
        mainAdapter?.onItemClick = { it ->
            run {
                eventDetail = it
                findNavController().navigate(R.id.action_EventsFragment_to_DetailsFragment)
            }
        }
    }
}
