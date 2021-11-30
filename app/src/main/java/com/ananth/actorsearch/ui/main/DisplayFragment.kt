package com.ananth.actorsearch.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ananth.actorsearch.R

class DisplayFragment : Fragment() {


    companion object {
        fun newInstance() = DisplayFragment()
    }

    val viewModel: DisplayViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // TODO: Use the ViewModel
        return inflater.inflate(R.layout.display_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = ActorAdapter()

        viewModel.celebrity.value?.let { celebrity ->
            if (recyclerView?.adapter is ActorAdapter){
                (recyclerView?.adapter as ActorAdapter).updateData(celebrity)
            }
        }
        viewModel.celebrity.observe(this, Observer { celeb ->
            if (recyclerView?.adapter is ActorAdapter){
                (recyclerView?.adapter as ActorAdapter).updateData(celeb)
            }
        })
    }

}