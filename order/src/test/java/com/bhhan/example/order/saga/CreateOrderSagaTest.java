package com.bhhan.example.order.saga;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.customer.api.channel.CustomerChannel;
import com.bhhan.example.customer.api.commands.ReserveCreditCommand;
import com.bhhan.example.customer.api.replies.CustomerCreditLimitExceeded;
import com.bhhan.example.customer.api.replies.CustomerNotFound;
import com.bhhan.example.order.api.common.OrderState;
import com.bhhan.example.order.api.common.RejectionReason;
import com.bhhan.example.order.common.vo.OrderDetails;
import com.bhhan.example.order.domain.Order;
import com.bhhan.example.order.domain.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.eventuate.tram.sagas.testing.SagaUnitTestSupport.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by hbh5274@gmail.com on 2020-11-20
 * Github : http://github.com/bhhan5274
 */
class CreateOrderSagaTest {
    private OrderRepository orderRepository;
    private Long customerId = 102L;
    private Money orderTotal = Money.wons(1000L);
    private OrderDetails orderDetails = OrderDetails.builder()
                                            .customerId(customerId)
                                            .orderTotal(orderTotal)
                                            .build();
    private Long orderId = 103L;
    private Order order;

    @BeforeEach
    void setup(){
        orderRepository = mock(OrderRepository.class);

        when(orderRepository.save(any(Order.class)))
                .then(invacation -> {
                    order = invacation.getArgument(0);
                    order.setId(orderId);
                    return order;
                });
        when(orderRepository.findById(orderId))
                .then(invocation -> Optional.of(order));
    }

    private CreateOrderSaga makeCreateOrderSaga(){
        return new CreateOrderSaga(orderRepository);
    }

    @Test
    @DisplayName("CreateOrder 성공")
    void test1(){
        given()
            .saga(makeCreateOrderSaga(), new CreateOrderSagaData(orderDetails))
        .expect()
            .command(ReserveCreditCommand.builder()
                .orderId(orderId)
                .customerId(customerId)
                .orderTotal(orderTotal)
                .build())
            .to(CustomerChannel.COMMAND_CHANNEL)
        .andGiven()
            .successReply()
        .expectCompletedSuccessfully();

        assertEquals(OrderState.APPROVED, order.getState());
    }

    @Test
    @DisplayName("CreateOrder 실패 > CustomerNotFound")
    void test2(){
        given()
            .saga(makeCreateOrderSaga(), new CreateOrderSagaData(orderDetails))
        .expect()
            .command(ReserveCreditCommand.builder()
                .orderId(orderId)
                .customerId(customerId)
                .orderTotal(orderTotal)
                .build())
            .to(CustomerChannel.COMMAND_CHANNEL)
        .andGiven()
            .failureReply(new CustomerNotFound())
        .expectRolledBack();

        assertEquals(RejectionReason.UNKNOWN_CUSTOMER, order.getRejectionReason());
        assertEquals(OrderState.REJECTED, order.getState());
    }

    @Test
    @DisplayName("CreateOrder 실패 > CustomerCreditLimitExceeded")
    void test3(){
        given()
                .saga(makeCreateOrderSaga(), new CreateOrderSagaData(orderDetails))
                .expect()
                .command(ReserveCreditCommand.builder()
                        .orderId(orderId)
                        .customerId(customerId)
                        .orderTotal(orderTotal)
                        .build())
                .to(CustomerChannel.COMMAND_CHANNEL)
                .andGiven()
                .failureReply(new CustomerCreditLimitExceeded())
                .expectRolledBack();

        assertEquals(RejectionReason.INSUFFICIENT_CREDIT, order.getRejectionReason());
        assertEquals(OrderState.REJECTED, order.getState());
    }
}