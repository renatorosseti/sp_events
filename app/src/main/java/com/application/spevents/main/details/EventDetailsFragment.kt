package com.application.spevents.main.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.application.spevents.R
import com.application.spevents.dialog.ProgressDialog
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_event_details.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.application.spevents.data.Cache.eventDetail
import com.application.spevents.dialog.MessageDialog
import com.application.spevents.main.model.Event
import com.application.spevents.main.model.NoNetworkException
import kotlinx.android.synthetic.main.fragment_event_details.bookButton

class EventDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var viewModel: EventDetailsViewModel

    @Inject
    lateinit var dialog: MessageDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observeActions(view)
        bookButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_DetailsFragment_to_BookEventFragment,
                bundleOf("eventId" to eventDetail.id)
            )
        }
    }

    private fun setUiView() {
        Glide.with(this)
            .load(eventDetail.image)
            .apply(RequestOptions().placeholder(R.mipmap.ic_event_foreground))
            .into(detailsImageView)

        titleTextView.text = eventDetail.title
        locationTextView.text = eventDetail.location
        descriptionTextView.text = eventDetail.description
        priceTextView.text = eventDetail.price
        dateTextView.text = eventDetail.date
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_DetailsFragment_to_EventsFragment)
                return true
            }
            R.id.action_share -> {
                navigateToSharingEvent(event = eventDetail)
                return true
            }
            R.id.action_location -> {
                navigateToLocationEvent(event = eventDetail)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeActions(view: View) {
        var snackbar: Snackbar = Snackbar.make(view, R.string.error_request, Snackbar.LENGTH_LONG)
        viewModel.response.observe(viewLifecycleOwner, Observer {
            when (it) {
                is EventDetailsViewState.RefreshEventDetails -> {
                    progressDialog.hide()
                    snackbar.dismiss()
                    setUiView()
                }
                is EventDetailsViewState.ShowLoadingState -> {
                    progressDialog.show(requireContext())
                }
                is EventDetailsViewState.ShowCheckInSucceed -> {
                    dialog.show(
                        context = requireContext(),
                        message = getString(R.string.succeed_check_in)
                    )
                }
                is EventDetailsViewState.ShowNetworkError -> {
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

    private fun navigateToLocationEvent(event: Event) {
        val labelLocation = "Location name"
        val urlAddress =
            "http://maps.google.com/maps?q=${event.latitude},${event.longitude}(${labelLocation})&iwloc=A&hl=es"
        val gmmIntentUri: Uri = Uri.parse(urlAddress)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context?.getPackageManager()) != null) {
            startActivity(mapIntent)
        }
    }

    private fun navigateToSharingEvent(event: Event) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
        val message =
            "${event.title}\n\n${event.description}\n\nR$${event.price}\n\n${event.image}"
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, "Compartilhar com"))
    }
}
