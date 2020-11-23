package com.bhhan.example.notification.service;

import com.bhhan.example.order.api.channel.OrderChannel;
import com.bhhan.example.order.api.events.OrderCreatedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by hbh5274@gmail.com on 2020-11-21
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {
    private final NotificationService notificationService;

    public DomainEventHandlers domainEventHandlers(){
        return DomainEventHandlersBuilder
                .forAggregateType(OrderChannel.ORDER_CHANNEL)
                .onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEvent)
                .build();
    }

    private void handleOrderCreatedEvent(DomainEventEnvelope<OrderCreatedEvent> envelope){
        log.info("handleOrderCreated called {}", envelope);
        notificationService.sendMessage(envelope.getEvent());
    }
}
