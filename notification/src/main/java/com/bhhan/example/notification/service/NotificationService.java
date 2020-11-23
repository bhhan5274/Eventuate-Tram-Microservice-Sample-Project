package com.bhhan.example.notification.service;

import com.bhhan.example.order.api.events.OrderCreatedEvent;

/**
 * Created by hbh5274@gmail.com on 2020-11-21
 * Github : http://github.com/bhhan5274
 */
public interface NotificationService {
    void sendMessage(OrderCreatedEvent orderCreatedEvent);
}
