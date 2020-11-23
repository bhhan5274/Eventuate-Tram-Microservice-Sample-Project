package com.bhhan.example.order.service;

import com.bhhan.example.order.api.events.OrderCreatedEvent;
import com.bhhan.example.order.common.vo.OrderDetails;
import com.bhhan.example.order.domain.Order;
import com.bhhan.example.order.domain.OrderRepository;
import com.bhhan.example.order.saga.CreateOrderSaga;
import com.bhhan.example.order.saga.CreateOrderSagaData;
import com.bhhan.example.order.service.dto.OrderDto;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

@Transactional
@NoArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private CreateOrderSaga createOrderSaga;
    private SagaInstanceFactory sagaInstanceFactory;
    private OrderDomainEventPublisher orderDomainEventPublisher;

    public OrderService(OrderRepository orderRepository, CreateOrderSaga createOrderSaga, SagaInstanceFactory sagaInstanceFactory,
                        OrderDomainEventPublisher orderDomainEventPublisher){
        this.orderRepository = orderRepository;
        this.createOrderSaga = createOrderSaga;
        this.sagaInstanceFactory = sagaInstanceFactory;
        this.orderDomainEventPublisher = orderDomainEventPublisher;
    }

    public OrderDto.OrderResponse createOrder(OrderDetails orderDetails){
        CreateOrderSagaData data = new CreateOrderSagaData(orderDetails);
        sagaInstanceFactory.create(createOrderSaga, data);

        Order order = orderRepository.findById(data.getOrderId())
                .orElseThrow(IllegalArgumentException::new);

        orderDomainEventPublisher.publish(order, Collections.singletonList(OrderCreatedEvent.builder().orderId(order.getId()).state(order.getState()).build()));

        return OrderDto.OrderResponse.create(order);
    }

    public List<OrderDto.OrderResponse> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findAllByOrderDetailsCustomerId(customerId);

        return orders.stream()
                .map(OrderDto.OrderResponse::create)
                .collect(Collectors.toList());
    }
}
