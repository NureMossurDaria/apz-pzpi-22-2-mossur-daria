package ua.nure.mossurd.blooddosyst.dto;

import ua.nure.mossurd.blooddosyst.entity.Notification;

public record NotificationDto(
        Integer id,
        String header,
        String body,
        Boolean delivered
) {
    public NotificationDto(Notification notification) {
        this(
                notification.getId(),
                notification.getNotificationHeader(),
                notification.getNotificationBody(),
                notification.getDelivered()
        );
    }
}
