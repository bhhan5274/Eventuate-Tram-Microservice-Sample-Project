package com.bhhan.example.customer;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.customer.api.commands.ReserveCreditCommand;
import com.bhhan.example.customer.api.replies.CustomerCreditLimitExceeded;
import com.bhhan.example.customer.config.CustomerConfiguration;
import com.bhhan.example.customer.service.CustomerService;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.spring.cloudcontractsupport.EventuateContractVerifierConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by hbh5274@gmail.com on 2020-11-22
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(classes = AbstractCustomerServiceConsumerContractTest.TestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public class AbstractCustomerServiceConsumerContractTest {

    @Configuration
    @Import({CustomerConfiguration.class, EventuateContractVerifierConfiguration.class})
    public static class TestConfiguration {
        @Bean
        public CustomerService customerService(){
            return mock(CustomerService.class);
        }
    }

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setup(){
        when(customerService.reserveCredit(any(ReserveCreditCommand.class)))
                .then(invocation -> {
                    ReserveCreditCommand cmd = (ReserveCreditCommand) invocation.getArguments()[0];

                    if(cmd.getOrderId() == 1){
                        return Money.wons(8000L);
                    }else {
                        return new CustomerCreditLimitExceeded();
                    }
                });
    }
}
