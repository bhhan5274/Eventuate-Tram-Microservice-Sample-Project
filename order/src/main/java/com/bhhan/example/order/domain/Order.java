package com.bhhan.example.order.domain;

import com.bhhan.example.order.api.common.OrderState;
import com.bhhan.example.order.api.common.RejectionReason;
import com.bhhan.example.order.common.vo.OrderDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @Embedded
    private OrderDetails orderDetails;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Enumerated(EnumType.STRING)
    private RejectionReason rejectionReason;

    @Version
    private Long version;

    public static Order createOrder(OrderDetails orderDetails){
        return new Order(orderDetails);
    }

    private Order(OrderDetails orderDetails){
        this.orderDetails = orderDetails;
        this.state = OrderState.PENDING;
    }

    public void approve(){
        this.state = OrderState.APPROVED;
    }

    public void reject(RejectionReason rejectionReason){
        this.state = OrderState.REJECTED;
        this.rejectionReason = rejectionReason;
    }
}
