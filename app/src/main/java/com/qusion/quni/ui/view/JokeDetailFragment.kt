package com.qusion.quni.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.qusion.quni.R
import com.qusion.quni.base.BaseFragment
import com.qusion.quni.databinding.FragmentJokeDetailBinding
import com.qusion.quni.ui.showErrorSnackbar
import com.qusion.quni.ui.viewmodel.JokeDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class JokeDetailFragment : BaseFragment<FragmentJokeDetailBinding>(R.layout.fragment_joke_detail) {

    private val jokeViewModel: JokeDetailViewModel by viewModel()

    override fun onBind() {

        bind.nextJokeButton.setOnClickListener {
            jokeViewModel.fetchNextRandomJoke()
        }

        bind.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jokeViewModel.jokeContent.observe(viewLifecycleOwner) {
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
        }

        // Error
        jokeViewModel.nextJokeError.observe(viewLifecycleOwner) {
            it?.consume()?.let {
                view.showErrorSnackbar(R.string.network_error_text)
            }
        }

        jokeViewModel.isJokeLoading.observe(viewLifecycleOwner) {
            bind.nextJokeButton.isEnabled = !it
        }
    }

    companion object {
        private const val TEXT_FADE_DURATION = 300L
    }
}
