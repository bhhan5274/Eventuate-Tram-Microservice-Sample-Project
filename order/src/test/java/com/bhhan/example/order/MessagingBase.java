package com.bhhan.example.order;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.order.api.events.OrderCreatedEvent;
import com.bhhan.example.order.common.vo.OrderDetails;
import com.bhhan.example.order.domain.Order;
import com.bhhan.example.order.service.OrderDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.cloudcontractsupport.EventuateContractVerifierConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.inmemory.TramInMemoryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;

/**
 * Created by hbh5274@gmail.com on 2020-11-21
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = MessagingBase.TestConfiguration.class)
@AutoConfigureMessageVerifier
public abstract class MessagingBase {

    @Configuration
    @EnableAutoConfiguration
    @Import({
            EventuateContractVerifierConfiguration.class,
            TramEventsPublisherConfiguration.class,
            TramInMemoryConfiguration.class
    })
    public static class TestConfiguration{
        @Bean
        public OrderDomainEventPublisher orderDomainEventPublisher(DomainEventPublisher domainEventPublisher){
            return new OrderDomainEventPublisher(domainEventPublisher);
        }
    }

    @Autowired
    private OrderDomainEventPublisher orderDomainEventPublisher;

    protected void orderCreatedEvent(){
        Order order = Order.createOrder(OrderDetails.builder().customerId(1L).orderTotal(Money.wons(1000L)).build());
        order.setId(99L);

        orderDomainEventPublisher.publish(order,
                Collections.singletonList(OrderCreatedEvent.builder().orderId(order.getId()).state(order.getState()).build()));
    }
}
