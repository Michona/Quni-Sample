package com.qusion.quni.ui.view

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.quni.apolloservice.api.NetworkResult
import com.qusion.quni.R
import com.qusion.quni.base.BaseFragment
import com.qusion.quni.chat.domain.ChatStoreRepository
import com.qusion.quni.databinding.FragmentLandingBinding
import com.qusion.quni.chat.domain.GoogleAuthenticator
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class LandingFragment : BaseFragment<FragmentLandingBinding>(R.layout.fragment_landing) {

    private val googleAuthenticator: GoogleAuthenticator by inject()
    private val chatStoreRepository: ChatStoreRepository by inject()

    override fun onBind() {

        bind.getJoke.setOnClickListener {
            findNavController().navigate(R.id.action_landing_to_detail)
        }

        bind.chatRoomButton.setOnClickListener {
            startActivityForResult(
                googleAuthenticator.getClientIntent(),
                GoogleAuthenticator.RC_SIGN_IN_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleAuthenticator.RC_SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                /* Just a shortcut - should use viewModelScope! (have this in a VM with some response live data. */
                lifecycleScope.launch {
                    when (
                        val response =
                            googleAuthenticator.signInWithCredentials(account.idToken)
                    ) {
                        is NetworkResult.Success -> {
                            Timber.d("${response.value.email}")

                            chatStoreRepository.cacheUser(response.value)
                            findNavController().navigate(R.id.action_landing_screen_to_chatRoomFragment)
                        }
                        is NetworkResult.Error -> {
                            Timber.e(response.cause)
                        }
                    }
                }
            } catch (e: ApiException) {
                Timber.e(e)
            }
        }
    }
}
