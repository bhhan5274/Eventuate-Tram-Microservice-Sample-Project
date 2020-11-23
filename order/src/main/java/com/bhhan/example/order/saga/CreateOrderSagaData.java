package com.bhhan.example.order.saga;

import com.bhhan.example.customer.api.commands.ReserveCreditCommand;
import com.bhhan.example.order.api.common.RejectionReason;
import com.bhhan.example.order.common.vo.OrderDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by hbh5274@gmail.com on 2020-11-20
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderSagaData {
    private OrderDetails orderDetails;
    private Long orderId;
    private RejectionReason rejectionReason;

    public CreateOrderSagaData(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ReserveCreditCommand makeReserveCreditCommand(){
        return new ReserveCreditCommand(orderId, orderDetails.getOrderTotal(), orderDetails.getCustomerId());
    }
}
