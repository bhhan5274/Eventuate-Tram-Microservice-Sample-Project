package com.bhhan.example.customer;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.customer.domain.Customer;
import com.bhhan.example.customer.domain.CustomerRepository;
import com.bhhan.example.customer.web.CustomerApi;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

/**
 * Created by hbh5274@gmail.com on 2020-11-19
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMessageVerifier
public class BaseTestClass {
    @Autowired
    private CustomerApi customerApi;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup(){
        customerRepository.save(Customer.builder()
        .name("bhhan")
        .saving(Money.wons(1000L))
        .build());

        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(customerApi);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
    }

    @AfterEach
    public void clear(){
        customerRepository.deleteAll();
    }
}
