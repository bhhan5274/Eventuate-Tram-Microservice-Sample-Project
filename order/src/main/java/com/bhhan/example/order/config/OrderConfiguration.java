package com.bhhan.example.order.config;

import com.bhhan.example.order.domain.OrderRepository;
import com.bhhan.example.order.saga.CreateOrderSaga;
import com.bhhan.example.order.service.OrderDomainEventPublisher;
import com.bhhan.example.order.service.OrderService;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration;
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

@Configuration
@Import({SagaOrchestratorConfiguration.class, OptimisticLockingDecoratorConfiguration.class,
        TramEventsPublisherConfiguration.class, TramMessageProducerJdbcConfiguration.class,
        EventuateTramKafkaMessageConsumerConfiguration.class})
public class OrderConfiguration {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public CreateOrderSaga createOrderSaga(OrderRepository orderRepository){
        return new CreateOrderSaga(orderRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository, SagaInstanceFactory sagaInstanceFactory,
                                     OrderDomainEventPublisher orderDomainEventPublisher, CreateOrderSaga createOrderSaga){
        return new OrderService(orderRepository, createOrderSaga, sagaInstanceFactory, orderDomainEventPublisher);
    }
}
