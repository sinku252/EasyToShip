package com.tws.courier.ui.chats

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tws.courier.R
import com.tws.courier.databinding.FragmentChatBinding
import com.tws.courier.databinding.FragmentHelpBinding
import com.tws.courier.databinding.FragmentTokenBinding
import com.tws.courier.domain.annotations.InputErrorType
import com.tws.courier.domain.models.Chat
import com.tws.courier.domain.models.Help
import com.tws.courier.ui.chats.adapter.ChatAdapter
import com.tws.courier.ui.help.HelpFragment
import com.tws.courier.ui.help.HelpViewModel
import com.tws.courier.ui.help.adapter.HelpAdapter
import com.tws.courier.ui.home.base.HomeBaseFragment

class ChatFragment : HomeBaseFragment<ChatViewModel, FragmentChatBinding>()
{
    companion object {
        val TAG = "ChatFragment"
        fun newInstance() = ChatFragment()
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.chats)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_chat
    override fun getViewModelClass(): Class<ChatViewModel> = ChatViewModel::class.java

    val adapter = ChatAdapter(
        object : ChatAdapter.AdapterCallbacks {
            override fun onSelectClicked(chat: Chat) {
              //  fragmentListener?.navigateToTokenResponseFragment()
            }
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
    }

    private fun initView()
    {
        viewBinding.reyclerviewMessageList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.reyclerviewMessageList.adapter =adapter
    }

    private fun observeLiveData()
    {

        viewModel.onChatListReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                adapter.setList(ArrayList(it))
            }
        })

        viewModel.onMsgSend.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                //var chat:Chat=Chat("",it,"customer")
                adapter.addMsg(it)
            }
        })
    }

    override fun showInputError(inputError: InputError) {
        when (inputError.errorType) {
            InputErrorType.MESSAGE -> {
                showMessageDialog(inputError.message)
            }

        }
    }
}