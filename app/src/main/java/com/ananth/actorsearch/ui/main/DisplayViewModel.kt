package com.ananth.actorsearch.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ananth.actorsearch.Celebrity

class DisplayViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var celebrity = MutableLiveData<Celebrity>()
}