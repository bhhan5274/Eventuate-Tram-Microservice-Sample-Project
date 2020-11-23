package com.bhhan.example.order.service;

import com.bhhan.example.order.web.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by hbh5274@gmail.com on 2020-11-20
 * Github : http://github.com/bhhan5274
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerServiceProxy{
    private final String CUSTOMER_URL = "http://localhost:9001/api/v1/customers/%d";
    private final RestTemplate restTemplate;

    @Override
    public boolean isValidCustomerId(Long customerId) {
        try {
            ResponseEntity<CustomerResponse> customerResponseEntity = restTemplate.exchange(String.format(CUSTOMER_URL, customerId), HttpMethod.GET, null, CustomerResponse.class);
            CustomerResponse body = customerResponseEntity.getBody();
            log.info("Received Customer Information: ID {}, Name {}, Saving {}", body.getId(), body.getName(), body.getSaving());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
