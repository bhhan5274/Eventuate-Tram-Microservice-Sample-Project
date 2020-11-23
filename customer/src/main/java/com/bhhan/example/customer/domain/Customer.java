package com.bhhan.example.customer.domain;

import com.bhhan.example.common.vo.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

@Entity
@Table(name = "CUSTOMERS")
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;

    private String name;

    @Embedded
    private Money saving;

    @Version
    private Long version;

    @Builder
    public Customer(Long id, String name, Money saving){
        this.id = id;
        this.name = name;
        this.saving = saving;
    }

    public void reserveCredit(Money orderTotal){
        if(isWithdrawable(orderTotal)){
            withdraw(orderTotal);
        }else {
            throw new CustomerCreditLimitExceededException();
        }
    }

    private void withdraw(Money orderTotal) {
        saving = saving.minus(orderTotal);
    }

    private boolean isWithdrawable(Money orderTotal) {
        return saving.minus(orderTotal).isGreaterThanOrEqual(Money.ZERO);
    }
}
