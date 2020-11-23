package com.bhhan.example.customer.web;

import com.bhhan.example.customer.service.CustomerService;
import com.bhhan.example.customer.service.dto.CustomerDto;
import com.bhhan.example.customer.service.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-19
 * Github : http://github.com/bhhan5274
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerApi {
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public CustomerDto.CustomerResponse getCustomerById(@PathVariable Long customerId){
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    public CustomerDto.CustomerResponse createCustomer(@RequestBody CustomerDto.CustomerRequest request){
        return customerService.createCustomer(request);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException e){
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }
}
