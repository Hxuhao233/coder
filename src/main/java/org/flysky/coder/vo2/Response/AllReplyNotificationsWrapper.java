package org.flysky.coder.vo2.Response;

import java.util.List;

public class AllReplyNotificationsWrapper {
    List<ReplyNotificationWrapper> oldReplyNotifications;
    List<ReplyNotificationWrapper> newReplyNotifications;

    public AllReplyNotificationsWrapper() {
    }

    public List<ReplyNotificationWrapper> getOldReplyNotifications() {
        return oldReplyNotifications;
    }

    public void setOldReplyNotifications(List<ReplyNotificationWrapper> oldReplyNotifications) {
        this.oldReplyNotifications = oldReplyNotifications;
    }

    public List<ReplyNotificationWrapper> getNewReplyNotifications() {
        return newReplyNotifications;
    }

    public void setNewReplyNotifications(List<ReplyNotificationWrapper> newReplyNotifications) {
        this.newReplyNotifications = newReplyNotifications;
    }
}
