package com.bhhan.example.order.service;

import com.bhhan.example.order.api.events.OrderDomainEvent;
import com.bhhan.example.order.domain.Order;
import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by hbh5274@gmail.com on 2020-11-21
 * Github : http://github.com/bhhan5274
 */

@Component
public class OrderDomainEventPublisher extends AbstractAggregateDomainEventPublisher<Order, OrderDomainEvent> {
    public OrderDomainEventPublisher(DomainEventPublisher domainEventPublisher){
        super(domainEventPublisher, Order.class, Order::getId);
    }
}
