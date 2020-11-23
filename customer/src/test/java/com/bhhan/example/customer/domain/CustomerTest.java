package com.bhhan.example.customer.domain;

import com.bhhan.example.common.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-19
 * Github : http://github.com/bhhan5274
 */

class CustomerTest {
    @Test
    @DisplayName("Customer 생성 테스트")
    void test1(){
        long id = 1L;
        String name = "bhhan";
        Money saving = Money.wons(1000L);

        Customer bhhan = Customer.builder()
                .id(id)
                .name(name)
                .saving(saving)
                .build();

        assertEquals(id, bhhan.getId());
        assertEquals(name, bhhan.getName());
        assertEquals(saving, bhhan.getSaving());
    }

    @Test
    @DisplayName("Customer 금액 인출 테스트")
    void test2(){
        Customer bhhan = Customer.builder()
                .id(1L)
                .name("bhhan")
                .saving(Money.wons(10000L))
                .build();

        bhhan.reserveCredit(Money.wons(3000L));

        assertEquals(Money.wons(7000L), bhhan.getSaving());
    }

    @Test
    @DisplayName("Customer 금액 인출 실패 테스트")
    void test3(){
        assertThrows(CustomerCreditLimitExceededException.class, () -> {
            Customer bhhan = Customer.builder()
                    .id(1L)
                    .name("bhhan")
                    .saving(Money.wons(10000L))
                    .build();

            bhhan.reserveCredit(Money.wons(30000L));
        });
    }
}