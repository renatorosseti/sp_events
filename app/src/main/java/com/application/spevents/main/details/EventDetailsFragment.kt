package com.application.spevents.main.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.application.spevents.R
import dagger.android.support.DaggerFragment
import com.application.spevents.data.Cache.eventDetail
import com.application.spevents.model.Event
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_event_details.*

class EventDetailsFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        setUiView()
        bookButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_DetailsFragment_to_BookEventFragment,
                bundleOf("eventId" to eventDetail.id)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_event_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            R.id.action_share -> {
                navigateToSharingEvent(event = eventDetail)
                true
            }
            R.id.action_location -> {
                navigateToLocationEvent(event = eventDetail)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUiView() {
        Glide.with(this)
            .load(eventDetail.image)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(detailsImageView)

        titleTextView.text = eventDetail.title
        locationTextView.text = eventDetail.location
        descriptionTextView.text = eventDetail.description
        priceTextView.text = eventDetail.price
        dateTextView.text = eventDetail.date
    }

    private fun navigateToLocationEvent(event: Event) {
        val labelLocation = getString(R.string.location)
        val urlAddress =
            "http://maps.google.com/maps?q=${event.latitude},${event.longitude}(${labelLocation})&iwloc=A&hl=es"
        val gmmIntentUri: Uri = Uri.parse(urlAddress)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context?.packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun navigateToSharingEvent(event: Event) {
        val labelLocation = getString(R.string.location)
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
        val message =
            "${event.title}\n\n${event.description}\n\n${event.price}\n\n${labelLocation}: ${event.location}"
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, getString(R.string.share_with)))
    }
}
