package com.qusion.quni.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.qusion.quni.R
import com.qusion.quni.base.BaseFragment
import com.qusion.quni.databinding.FragmentJokeDetailBinding
import com.qusion.quni.ui.setVisibility
import com.qusion.quni.ui.showErrorSnackbar
import com.qusion.quni.ui.viewmodel.JokeDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class JokeDetailFragment : BaseFragment<FragmentJokeDetailBinding>(R.layout.fragment_joke_detail) {

    private val jokeViewModel: JokeDetailViewModel by viewModel()

    override fun onBind() {

        bind.nextJokeButton.setOnClickListener {
            jokeViewModel.fetchNextRandomJoke()
        }

        bind.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        bind.favouritesButton.setOnClickListener {
            showAlpaca()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jokeViewModel.jokeContent.observe(viewLifecycleOwner, Observer {
            hideAlpaca()

            bind.jokeText
                .animate()
                .alpha(0f)
                .setDuration(TEXT_FADE_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        bind.jokeText.text = it
                        bind.jokeText.animate().alpha(1f).setDuration(TEXT_FADE_DURATION).start()
                    }
                }).start()
        })

        //Error
        jokeViewModel.nextJokeError.observe(viewLifecycleOwner, Observer {
            it?.consume()?.let {
                view.showErrorSnackbar(R.string.network_error_text)
            }
        })

        jokeViewModel.isJokeLoading.observe(viewLifecycleOwner, Observer {
            bind.nextJokeButton.isEnabled = !it
        })
    }

    /* Just a tiny Alpaca easter egg */
    private fun hideAlpaca() {
        bind.jokeText.visibility = View.VISIBLE
        bind.jokeNumber.visibility = View.VISIBLE

        bind.eeAlpaca.visibility = View.GONE
        bind.eeAlpacaText.visibility = View.GONE
    }
    private fun showAlpaca() {
        bind.jokeText.visibility = View.GONE
        bind.jokeNumber.visibility = View.GONE

        bind.eeAlpaca.visibility = View.VISIBLE
        bind.eeAlpacaText.visibility = View.VISIBLE
    }

    companion object {
        private const val TEXT_FADE_DURATION = 300L
    }
}