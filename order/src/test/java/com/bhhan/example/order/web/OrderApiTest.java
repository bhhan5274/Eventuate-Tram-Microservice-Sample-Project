package com.bhhan.example.order.web;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.order.common.vo.OrderDetails;
import com.bhhan.example.order.domain.Order;
import com.bhhan.example.order.service.CustomerServiceProxy;
import com.bhhan.example.order.service.OrderService;
import com.bhhan.example.order.service.dto.OrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by hbh5274@gmail.com on 2020-11-19
 * Github : http://github.com/bhhan5274
 */

@WebMvcTest(OrderApi.class)
class OrderApiTest {
    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("CreateOrder 성공 테스트")
    void test1() throws Exception {
        OrderDetails orderDetails = OrderDetails.builder()
                .customerId(1L)
                .orderTotal(Money.wons(500L))
                .build();

        when(orderService.createOrder(any(OrderDetails.class)))
                .then(invocation -> {
                    Order order = Order.createOrder((OrderDetails) invocation.getArguments()[0]);
                    order.setId(101L);
                    return OrderDto.OrderResponse.create(order);
                });

        when(customerServiceProxy.isValidCustomerId(any(Long.class)))
                .thenReturn(true);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDetails)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("CreateOrder 실패 테스트")
    void test2() throws Exception {
        OrderDetails orderDetails = OrderDetails.builder()
                .customerId(2L)
                .orderTotal(Money.wons(500L))
                .build();

        when(customerServiceProxy.isValidCustomerId(any(Long.class)))
                .thenReturn(false);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDetails)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}