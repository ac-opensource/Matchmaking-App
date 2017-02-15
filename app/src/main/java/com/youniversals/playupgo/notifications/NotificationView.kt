package com.youniversals.playupgo.notifications

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Notification
import kotlinx.android.synthetic.main.notification_list_view_item.view.*

class NotificationView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var notificationCard: View? = null

    var data: Notification? = null

    var message: String? = null
        set(value) {
            field = value
            notificationMessageTextView.text = value
        }

    var date: String? = null
        set(value) {
            field = value
            notificationDateTextView.text = value
        }

    init {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.notification_list_view_item, this)
        notificationCard = notificationCardView
    }

    fun clear() {
    }

}