package com.bhhan.example.order.saga;

import com.bhhan.example.customer.api.replies.CompletedPayment;
import com.bhhan.example.customer.api.replies.CustomerCreditLimitExceeded;
import com.bhhan.example.customer.api.replies.CustomerNotFound;
import com.bhhan.example.order.api.common.RejectionReason;
import com.bhhan.example.order.domain.Order;
import com.bhhan.example.order.domain.OrderRepository;
import com.bhhan.example.order.sagaparticipants.CustomerServiceMessageProxy;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by hbh5274@gmail.com on 2020-11-20
 * Github : http://github.com/bhhan5274
 */

@Slf4j
public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaData> {
    private OrderRepository orderRepository;

    public CreateOrderSaga(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    private SagaDefinition<CreateOrderSagaData> sagaDefinition = step()
            .invokeLocal(this::create)
            .withCompensation(this::reject)
        .step()
            .invokeParticipant(CustomerServiceMessageProxy.reserveCredit, CreateOrderSagaData::makeReserveCreditCommand)
            .onReply(CustomerNotFound.class, this::handleCustomerNotFound)
            .onReply(CustomerCreditLimitExceeded.class, this::handleCustomerCreditLimitExceeded)
            .onReply(CompletedPayment.class, this::handleCompletedPayment)
        .step()
            .invokeLocal(this::approve)
        .build();

    @Override
    public SagaDefinition<CreateOrderSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }

    private void handleCustomerNotFound(CreateOrderSagaData data, CustomerNotFound reply){
        data.setRejectionReason(RejectionReason.UNKNOWN_CUSTOMER);
    }

    private void handleCompletedPayment(CreateOrderSagaData data, CompletedPayment reply){
        log.info("Order Saga: received saving {}", reply.getSaving());
    }

    private void handleCustomerCreditLimitExceeded(CreateOrderSagaData data, CustomerCreditLimitExceeded reply){
        data.setRejectionReason(RejectionReason.INSUFFICIENT_CREDIT);
    }

    private void create(CreateOrderSagaData data){
        Order order = orderRepository.save(Order.createOrder(data.getOrderDetails()));
        data.setOrderId(order.getId());
    }

    private void reject(CreateOrderSagaData data){
        orderRepository.findById(data.getOrderId())
                .orElseThrow(IllegalArgumentException::new)
                .reject(data.getRejectionReason());
    }

    private void approve(CreateOrderSagaData data){
        orderRepository.findById(data.getOrderId())
                .orElseThrow(IllegalArgumentException::new)
                .approve();
    }
}
