package com.youniversals.playupgo.notifications

import android.content.Context
import android.support.annotation.LayoutRes
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyModel
import com.youniversals.playupgo.R
import com.youniversals.playupgo.data.Notification


/**
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 28/12/2016
 */

class NotificationListAdapter(var onClickListener: View.OnClickListener) : EpoxyAdapter() {
    fun addNotifications(notifications: Collection<Notification>?) {
        notifications?.forEach {
            addModel(NotificationModel(it, onClickListener))
        }

        if (notifications == null || notifications.isEmpty()) {
            addModel(NoNotificationsModel())
        }
    }
}

class NotificationModel(private val notification: Notification, private val onClickListener: View.OnClickListener) : EpoxyModel<NotificationView>() {

    init {
        id(notification.id)
    }

    @LayoutRes
    public override fun getDefaultLayout(): Int {
        return R.layout.notification_model
    }

    override fun bind(notificationView: NotificationView?) {
        notificationView?.data = notification
        notificationView?.message = notification.message
        notificationView?.date = DateUtils.getRelativeTimeSpanString(notification.date, System.currentTimeMillis(), 0).toString()
        notificationView?.setOnClickListener(onClickListener)
    }

    override fun unbind(notificationView: NotificationView?) {
        super.unbind(notificationView)
        notificationView?.setOnClickListener(null)
    }
}

class NoNotificationsModel : EpoxyModel<NoNotificationView>() {
    init {
        id(0)
    }
    override fun getDefaultLayout(): Int {
        return R.layout.notification_model_nothing_found
    }
}

class NoNotificationView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.notification_list_view_item_nothing_found, this)
    }

}