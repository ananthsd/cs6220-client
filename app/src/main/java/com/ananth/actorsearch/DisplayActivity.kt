package com.ananth.actorsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.ananth.actorsearch.ui.main.DisplayFragment
import com.ananth.actorsearch.ui.main.DisplayViewModel

class DisplayActivity : AppCompatActivity() {
    val displayViewModel: DisplayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_activity)
        val celeb = intent.getParcelableExtra<Celebrity>("celebrity")
        celeb?.let { displayViewModel.celebrity.value = it }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DisplayFragment.newInstance())
                .commitNow()
        }
    }
}