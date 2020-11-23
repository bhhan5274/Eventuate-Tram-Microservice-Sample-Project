package com.bhhan.example.order.service.dto;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.order.api.common.OrderState;
import com.bhhan.example.order.api.common.RejectionReason;
import com.bhhan.example.order.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */
public class OrderDto {

    @Getter
    @Service
    @NoArgsConstructor
    public static class OrderResponse {
        private Long id;
        private OrderState state;
        private RejectionReason rejectionReason;
        private Long customerId;
        private Money orderTotal;

        public static OrderResponse create(Order order){
            return new OrderResponse(order);
        }

        private OrderResponse(Order order){
            this.id = order.getId();
            this.state = order.getState();
            this.rejectionReason = order.getRejectionReason();
            this.customerId = order.getOrderDetails().getCustomerId();
            this.orderTotal = order.getOrderDetails().getOrderTotal();
        }
    }
}
