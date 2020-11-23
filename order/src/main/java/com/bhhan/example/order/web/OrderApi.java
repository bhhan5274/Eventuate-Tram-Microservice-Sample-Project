package com.bhhan.example.order.web;

import com.bhhan.example.order.common.vo.OrderDetails;
import com.bhhan.example.order.service.CustomerServiceProxy;
import com.bhhan.example.order.service.OrderService;
import com.bhhan.example.order.service.dto.OrderDto;
import com.bhhan.example.order.web.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */

@RestController
@RequestMapping(value = "/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderApi {
    private final OrderService orderService;
    private final CustomerServiceProxy customerServiceProxy;

    @GetMapping("/{customerId}")
    public List<OrderDto.OrderResponse> getOrdersByCustomerId(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    @PostMapping
    public OrderDto.OrderResponse createOrder(@RequestBody OrderDetails orderDetails) {
        if(customerServiceProxy.isValidCustomerId(orderDetails.getCustomerId())){
            return orderService.createOrder(orderDetails);
        }else {
            throw new CustomerNotFoundException(String.format("Not found Customer Id: %d", orderDetails.getCustomerId()));
        }
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }
}
