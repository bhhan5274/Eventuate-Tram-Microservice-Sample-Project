package com.bhhan.example.common.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */
class MoneyTest {
    @Test
    @DisplayName("1000원 + 1200원 = 2200원")
    void test1(){
        Money money1 = Money.wons(1000L);
        Money money2 = Money.wons(1200L);
        assertEquals(Money.wons(2200L), money1.plus(money2));
    }

    @Test
    @DisplayName("1000원 * 3 = 3000원")
    void test2(){
        Money money = Money.wons(1000L);
        assertEquals(Money.wons(3000L), money.times(3L));
    }

    @Test
    @DisplayName("1000원 + 10000원 + 1234원 = 12234원")
    void test3(){
        List<Money> monies = Arrays.asList(Money.wons(1000L), Money.wons(10000L), Money.wons(1234L));

        Money totalSum = Money.sum(monies, Function.identity());
        assertEquals(Money.wons(12234L), totalSum);
    }
}