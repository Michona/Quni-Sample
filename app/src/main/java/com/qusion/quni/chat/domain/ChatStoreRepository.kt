package com.qusion.quni.chat.domain

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.qusion.quni.chat.entities.ChatMessageDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatStoreRepository {

    private val _db = Firebase.firestore

    private var _user: FirebaseUser? = null

    private val _messagesCollectionQuery: CollectionReference by lazy {
        _db.collection("rooms")
            .document(BASE_ROOM_ID)
            .collection("messages")
    }

    /** When Auth is successful */
    fun cacheUser(it: FirebaseUser) {
        _user = it
    }

    @ExperimentalCoroutinesApi
    fun getMessagesFlow() = callbackFlow {

        val snapshotListener =
            EventListener<QuerySnapshot> { value, error ->

                if (error != null) {
                    close(cause = error)
                    return@EventListener
                }

                if (value != null) {
                    offer(
                        value.documents.map {
                            ChatMessageDto(
                                from = it.getString("from") ?: "",
                                text = it.getString("text") ?: ""
                            )
                        }
                    )
                }
            }

        val snapshotQuery = _messagesCollectionQuery
            .addSnapshotListener(MetadataChanges.INCLUDE, snapshotListener)

        awaitClose {
            snapshotQuery.remove()
        }
    }

    suspend fun saveMessage(text: String) {
        _messagesCollectionQuery.document()
            .set(
                hashMapOf(
                    "from" to (_user?.displayName ?: ""),
                    "text" to text
                )
            )
            .await()
    }

    companion object {
        private const val BASE_ROOM_ID = "JkmyKcwEWyo7Le4vuWBn"
    }
}
