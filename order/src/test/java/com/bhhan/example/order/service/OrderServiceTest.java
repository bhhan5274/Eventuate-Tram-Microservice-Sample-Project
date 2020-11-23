package com.bhhan.example.order.service;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.order.api.common.OrderState;
import com.bhhan.example.order.api.events.OrderCreatedEvent;
import com.bhhan.example.order.common.vo.OrderDetails;
import com.bhhan.example.order.domain.Order;
import com.bhhan.example.order.domain.OrderRepository;
import com.bhhan.example.order.saga.CreateOrderSaga;
import com.bhhan.example.order.saga.CreateOrderSagaData;
import com.bhhan.example.order.service.dto.OrderDto;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private SagaInstanceFactory sagaInstanceFactory;
    private CreateOrderSaga createOrderSaga;
    private OrderDomainEventPublisher orderDomainEventPublisher;

    @BeforeEach
    void init() {
        orderRepository = mock(OrderRepository.class);
        sagaInstanceFactory = mock(SagaInstanceFactory.class);
        createOrderSaga = mock(CreateOrderSaga.class);
        orderDomainEventPublisher = mock(OrderDomainEventPublisher.class);
        orderService = new OrderService(orderRepository, createOrderSaga, sagaInstanceFactory, orderDomainEventPublisher);
    }

    @Test
    @DisplayName("Order 생성 테스트")
    void test1() {
        final Long orderId = 1L;
        final long customerId = 101L;
        final Money orderTotal = Money.wons(1000L);

        final Order order = Order.createOrder(OrderDetails.builder()
                .customerId(customerId)
                .orderTotal(orderTotal)
                .build());
        order.setId(orderId);

        when(orderRepository.findById(any()))
                .thenReturn(Optional.of(order));

        OrderDto.OrderResponse orderResponse = createOrder(customerId, orderTotal);

        verify(sagaInstanceFactory).create(same(createOrderSaga), any(CreateOrderSagaData.class));
        verify(orderDomainEventPublisher).publish(same(order), anyList());

        assertEquals(orderId, orderResponse.getId());
        assertEquals(customerId, orderResponse.getCustomerId());
        assertEquals(orderTotal, orderResponse.getOrderTotal());
        assertEquals(OrderState.PENDING, orderResponse.getState());
        assertNull(orderResponse.getRejectionReason());
    }

    @Test
    @DisplayName("OrderByCustomerId 테스트")
    void test2() {
        final long customerId1 = 1L;
        final long customerId2 = 2L;
        final List<Order> orders = Arrays.asList(
                makeOrder(customerId1, Money.wons(1000L)),
                makeOrder(customerId2, Money.wons(2000L)),
                makeOrder(customerId1, Money.wons(3000L)),
                makeOrder(customerId2, Money.wons(4000L)),
                makeOrder(customerId1, Money.wons(5000L))
        );

        when(orderRepository.findAllByOrderDetailsCustomerId(any(Long.class)))
                .then(invocation -> orders.stream()
                        .filter(order -> order.getOrderDetails().getCustomerId() == invocation.getArguments()[0])
                        .collect(toList()));

        List<OrderDto.OrderResponse> findOrders = orderService.getOrdersByCustomerId(customerId1);
        assertEquals(3, findOrders.size());
    }

    private OrderDto.OrderResponse createOrder(long customerId, Money orderTotal) {
        OrderDetails orderDetails = OrderDetails.builder()
                .customerId(customerId)
                .orderTotal(orderTotal)
                .build();

        return orderService.createOrder(orderDetails);
    }

    private Order makeOrder(long customerId, Money orderTotal){
        OrderDetails orderDetails = OrderDetails.builder()
                .customerId(customerId)
                .orderTotal(orderTotal)
                .build();

        return Order.createOrder(orderDetails);
    }
}