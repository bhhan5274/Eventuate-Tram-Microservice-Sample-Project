package com.bhhan.example.order.common.vo;

import com.bhhan.example.common.vo.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class OrderDetails {
    private Long customerId;
    @Embedded
    private Money orderTotal;

    @Builder
    public OrderDetails(Long customerId, Money orderTotal){
        this.customerId = customerId;
        this.orderTotal = orderTotal;
    }
}
