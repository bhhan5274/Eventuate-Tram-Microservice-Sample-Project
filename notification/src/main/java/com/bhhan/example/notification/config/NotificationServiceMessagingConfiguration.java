package com.bhhan.example.notification.config;

import com.bhhan.example.notification.service.NotificationService;
import com.bhhan.example.notification.service.OrderEventHandler;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by hbh5274@gmail.com on 2020-11-21
 * Github : http://github.com/bhhan5274
 */

@Configuration
@Import({TramEventSubscriberConfiguration.class,
        OptimisticLockingDecoratorConfiguration.class})
public class NotificationServiceMessagingConfiguration {
    @Bean
    public OrderEventHandler orderEventHandler(NotificationService notificationService){
        return new OrderEventHandler(notificationService);
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(OrderEventHandler orderEventHandler, DomainEventDispatcherFactory eventDispatcherFactory){
        return eventDispatcherFactory.make("notificationServiceDomainEventDispatcher", orderEventHandler.domainEventHandlers());
    }
}
