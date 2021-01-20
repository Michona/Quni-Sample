package com.qusion.quni.chat.domain

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.quni.apolloservice.api.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GoogleAuthenticator(private val context: Context) {

    private val client by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso)
    }

    fun getClientIntent() = client.signInIntent

    suspend fun signInWithCredentials(idToken: String?): NetworkResult<FirebaseUser> =
        withContext(Dispatchers.IO) {
            val credentials = GoogleAuthProvider.getCredential(idToken, null)
            return@withContext try {
                val response = Firebase.auth.signInWithCredential(credentials).await()
                NetworkResult.Success(response.user!!)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }

    companion object {
        private const val WEB_CLIENT_ID =
            "922826577424-fcivpjf9ot351hrguj0mr5qbqnj7mve5.apps.googleusercontent.com"

        const val RC_SIGN_IN_CODE = 169
    }
}
