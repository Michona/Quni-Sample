package com.qusion.quni.ui.view

import androidx.navigation.fragment.findNavController
import com.qusion.quni.R
import com.qusion.quni.base.BaseFragment
import com.qusion.quni.databinding.FragmentLandingBinding

class LandingFragment : BaseFragment<FragmentLandingBinding>(R.layout.fragment_landing) {
    override fun onBind() {

        bind.getJoke.setOnClickListener {
            findNavController().navigate(R.id.action_landing_to_detail)
        }
    }
}
