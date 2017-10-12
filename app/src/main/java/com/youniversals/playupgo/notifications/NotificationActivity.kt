package com.youniversals.playupgo.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.BaseActivity
import com.youniversals.playupgo.flux.action.NotificationActionCreator
import com.youniversals.playupgo.flux.action.NotificationActionCreator.Companion.ACTION_GET_NOTIF_S
import com.youniversals.playupgo.flux.store.NotificationStore
import kotlinx.android.synthetic.main.activity_notification.*
import javax.inject.Inject

class NotificationActivity : BaseActivity() {
    @Inject lateinit var notificationActionCreator: NotificationActionCreator
    @Inject lateinit var notificationStore: NotificationStore

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var notificationAdapter: NotificationListAdapter

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        PlayUpApplication.fluxComponent.inject(this)
        title = "Notifications"
        layoutManager = LinearLayoutManager(this)
        notificationAdapter = NotificationListAdapter(View.OnClickListener { v ->

        })
        notificationRecyclerView.setHasFixedSize(true)
        notificationRecyclerView.layoutManager = layoutManager
        notificationRecyclerView.adapter = notificationAdapter
        initFlux()
    }

    private fun initFlux() {
        notificationStore.observableWithFilter(ACTION_GET_NOTIF_S)
                .subscribe {
                    runOnUiThreadIfAlive(Runnable {
                        notificationAdapter.addNotifications(it.notifications)

                    })
                }
        notificationActionCreator.getNotifications()
    }

}
