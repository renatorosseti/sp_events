package com.application.spevents.main

import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.application.spevents.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    val navController = findNavController(R.id.nav_host_fragment)

    navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
      supportActionBar?.setDisplayHomeAsUpEnabled(destination.id != R.id.EventsFragment)
    })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }
}
