package com.bhhan.example.customer.api.commands;

import com.bhhan.example.common.vo.Money;
import io.eventuate.tram.commands.common.Command;
import lombok.Builder;
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
public class ReserveCreditCommand implements Command {
    private Long orderId;
    private Money orderTotal;
    private Long customerId;

    @Builder
    public ReserveCreditCommand(Long orderId, Money orderTotal, Long customerId){
        this.orderId = orderId;
        this.orderTotal = orderTotal;
        this.customerId = customerId;
    }
}
