package com.qusion.quni.chat.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.quni.apolloservice.api.NetworkResult
import com.qusion.quni.R
import com.qusion.quni.base.BaseFragment
import com.qusion.quni.databinding.ChatRoomFragmentBinding
import com.qusion.quni.chat.domain.ChatStoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import timber.log.Timber

class ChatRoomFragment : BaseFragment<ChatRoomFragmentBinding>(R.layout.chat_room_fragment) {

    private val chatStoreRepository: ChatStoreRepository by inject()

    override fun onBind() {

        bind.sendButton.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                chatStoreRepository.saveMessage(text = bind.messageEditText.text?.toString() ?: "")
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            chatStoreRepository.getMessagesFlow().collect {
                it.forEach {
                    Timber.d("$it")
                }

                bind.lastMessage.text =
                    it.joinToString(separator = "\n \n") { "${it.from}: ${it.text}" }
            }
        }
    }
}