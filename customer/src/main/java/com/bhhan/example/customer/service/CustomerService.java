package com.bhhan.example.customer.service;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.customer.api.commands.ReserveCreditCommand;
import com.bhhan.example.customer.domain.Customer;
import com.bhhan.example.customer.domain.CustomerRepository;
import com.bhhan.example.customer.service.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bhhan.example.customer.service.dto.CustomerDto.CustomerRequest;
import static com.bhhan.example.customer.service.dto.CustomerDto.CustomerResponse;

/**
 * Created by hbh5274@gmail.com on 2020-11-19
 * Github : http://github.com/bhhan5274
 */

@Transactional
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Money reserveCredit(ReserveCreditCommand reserveCreditCommand){
        Customer customer = customerRepository.findById(reserveCreditCommand.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Not Found CustomerId: %d", reserveCreditCommand.getCustomerId())));

        customer.reserveCredit(reserveCreditCommand.getOrderTotal());

        return customer.getSaving();
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        return CustomerResponse
                .create(customerRepository.save(request.toCustomer()));
    }

    public CustomerResponse getCustomerById(Long customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Not Found CustomerId: %d", customerId)));

        return CustomerResponse.create(customer);
    }
}
