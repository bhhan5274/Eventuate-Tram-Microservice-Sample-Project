package com.bhhan.example.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-11-17
 * Github : http://github.com/bhhan5274
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderDetailsCustomerId(Long customerId);
}
