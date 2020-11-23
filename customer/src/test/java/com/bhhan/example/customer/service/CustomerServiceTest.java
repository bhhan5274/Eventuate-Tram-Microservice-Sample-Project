package com.bhhan.example.customer.service;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.customer.domain.Customer;
import com.bhhan.example.customer.domain.CustomerRepository;
import com.bhhan.example.customer.service.dto.CustomerDto;
import com.bhhan.example.customer.service.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-20
 * Github : http://github.com/bhhan5274
 */

class CustomerServiceTest {
    private CustomerService customerService;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup(){
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    @DisplayName("CreateCustomer 테스트")
    void test1(){
        final Long userId = 1L;
        String name = "bhhan";
        Money saving = Money.wons(10000L);

        when(customerRepository.save(any(Customer.class)))
                .then(invacation -> {
                    Customer customer = (Customer) invacation.getArguments()[0];
                    customer.setId(userId);
                    return customer;
                });

        CustomerDto.CustomerResponse customerResponse = customerService.createCustomer(CustomerDto.CustomerRequest.builder()
                .name(name)
                .saving(saving)
                .build());

        verify(customerRepository).save(any(Customer.class));
        assertEquals(userId, customerResponse.getId());
        assertEquals(name, customerResponse.getName());
        assertEquals(saving, customerResponse.getSaving());
    }

    @Test
    @DisplayName("GetCustomerById 테스트")
    void test2(){
        long userId = 1L;
        String name = "bhhan";
        Money saving = Money.wons(10000L);

        when(customerRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(Customer.builder()
                        .id(userId)
                        .name(name)
                        .saving(saving)
                        .build()));

        CustomerDto.CustomerResponse customerResponse = customerService.getCustomerById(1L);

        verify(customerRepository).findById(1L);
        assertEquals(userId, customerResponse.getId());
        assertEquals(name, customerResponse.getName());
        assertEquals(saving, customerResponse.getSaving());
    }

    @Test
    @DisplayName("GetCustomerById 실패 테스트")
    void test3(){
        when(customerRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
             customerService.getCustomerById(1L);
        });
    }
}