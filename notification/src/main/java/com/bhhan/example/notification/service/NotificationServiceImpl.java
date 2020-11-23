package com.bhhan.example.notification.service;

import com.bhhan.example.order.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by hbh5274@gmail.com on 2020-11-21
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService{
    @Override
    public void sendMessage(OrderCreatedEvent orderCreatedEvent) {
        log.info("send Message[ID: {}]", orderCreatedEvent.getOrderId());
    }
}
