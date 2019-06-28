package com.example.im.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.im.R
import com.example.im.adapter.ContactListAdapter
import com.example.im.adapter.EMContactListenerAdapter
import com.example.im.contract.ContactContract
import com.example.im.presenter.ContactPresenter
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

/**
 * @author  Mloong
 * date  2019/6/28 19:26
 */

class ContactFragment :BaseFragment(),ContactContract.View{

    val presenter = ContactPresenter(this)



    override fun onLoadContactSuccess() {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter?.notifyDataSetChanged()


    }

    override fun onLoadContactsFailed() {
        swipeRefreshLayout.isRefreshing = false

        context?.toast(R.string.load_contacts_failed)
    }

    override fun getLayoutResId(): Int  = R.layout.fragment_contacts

    override fun init() {
        super.init()

        headerTitle.text = getString(R.string.contact)
        add.visibility = View.VISIBLE

        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.qq_blue)
            isRefreshing = true

            setOnRefreshListener { presenter.loadContacts() }
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter =  ContactListAdapter(context,presenter.contactListItems)


        }


        EMClient.getInstance().contactManager().setContactListener(object : EMContactListenerAdapter(){

            override fun onContactDeleted(p0: String?) {
                //重新获取联系人的数据
                presenter.loadContacts()

            }

        })

        presenter.loadContacts()

    }

}