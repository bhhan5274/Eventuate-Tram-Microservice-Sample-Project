package com.bhhan.example.notification.service;

import com.bhhan.example.notification.config.NotificationServiceMessagingConfiguration;
import com.bhhan.example.order.api.common.OrderState;
import com.bhhan.example.order.api.events.OrderCreatedEvent;
import io.eventuate.tram.messaging.common.ChannelMapping;
import io.eventuate.tram.messaging.common.DefaultChannelMapping;
import io.eventuate.tram.spring.cloudcontractsupport.EventuateContractVerifierConfiguration;
import io.eventuate.tram.spring.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.spring.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import io.eventuate.tram.spring.inmemory.TramInMemoryConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static io.eventuate.util.test.async.Eventually.eventually;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-21
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest(classes = OrderEventHandlerTest.TestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = {"com.bhhan:order:+:stubs"})
class OrderEventHandlerTest {
    @Configuration
    @EnableAutoConfiguration
    @Import({
            NotificationServiceMessagingConfiguration.class,
            TramCommandProducerConfiguration.class,
            TramInMemoryConfiguration.class,
            EventuateContractVerifierConfiguration.class
    })
    public static class TestConfiguration{
        @Bean
        public ChannelMapping channelMapping(){
            return new DefaultChannelMapping.DefaultChannelMappingBuilder().build();
        }

        @Bean
        public NotificationService notificationService(){
            return mock(NotificationService.class);
        }
    }

    @Autowired
    private StubFinder stubFinder;

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("handleOrderCreatedEvent 테스트")
    void test1(){
        stubFinder.trigger("orderCreatedForNotificationService");

        eventually(() -> {
            ArgumentCaptor<OrderCreatedEvent> eventArgumentCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);

            verify(notificationService).sendMessage(eventArgumentCaptor.capture());

            OrderCreatedEvent orderCreatedEvent = eventArgumentCaptor.getValue();

            assertEquals(99L, orderCreatedEvent.getOrderId());
            assertEquals(OrderState.PENDING, orderCreatedEvent.getState());
        });
    }
}