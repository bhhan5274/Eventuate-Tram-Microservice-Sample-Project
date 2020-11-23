package com.bhhan.example.order;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.customer.api.commands.ReserveCreditCommand;
import com.bhhan.example.customer.api.replies.CompletedPayment;
import com.bhhan.example.order.saga.CreateOrderSaga;
import com.bhhan.example.order.sagaparticipants.CustomerServiceMessageProxy;
import io.eventuate.tram.sagas.spring.inmemory.TramSagaInMemoryConfiguration;
import io.eventuate.tram.sagas.spring.testing.contract.EventuateTramSagasSpringCloudContractSupportConfiguration;
import io.eventuate.tram.sagas.spring.testing.contract.SagaMessagingTestHelper;
import io.eventuate.tram.spring.cloudcontractsupport.EventuateTramRoutesConfigurer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.BatchStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by hbh5274@gmail.com on 2020-11-22
 * Github : http://github.com/bhhan5274
 */

@ActiveProfiles("test")
@SpringBootTest(classes = CustomerServiceMessageProxyProxyIntegrationTest.TestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = {"com.bhhan:customer:+:stubs"})
@DirtiesContext
public class CustomerServiceMessageProxyProxyIntegrationTest {

    @Configuration
    @EnableAutoConfiguration
    @Import({
            TramSagaInMemoryConfiguration.class,
            EventuateTramSagasSpringCloudContractSupportConfiguration.class
    })
    public static class TestConfiguration{

        @Bean
        public EventuateTramRoutesConfigurer eventuateTramRoutesConfigurer(BatchStubRunner batchStubRunner){
            return new EventuateTramRoutesConfigurer(batchStubRunner);
        }

        @Bean
        public CustomerServiceMessageProxy customerService(){
            return new CustomerServiceMessageProxy();
        }
    }

    @Autowired
    private SagaMessagingTestHelper sagaMessagingTestHelper;

    @Autowired
    private CustomerServiceMessageProxy customerServiceMessageProxy;

    @Test
    @DisplayName("ReservedCredit 성공")
    void test1(){
        ReserveCreditCommand cmd = ReserveCreditCommand.builder()
                .orderId(1L)
                .customerId(1L)
                .orderTotal(Money.wons(1000L))
                .build();

        CompletedPayment expectedReply = CompletedPayment.builder()
                .saving(Money.wons(8000L))
                .build();

        String sagaType = CreateOrderSaga.class.getName();

        CompletedPayment reply = sagaMessagingTestHelper.sendAndReceiveCommand(customerServiceMessageProxy.reserveCredit,
                cmd, CompletedPayment.class, sagaType);

        assertEquals(expectedReply, reply);
    }
}
