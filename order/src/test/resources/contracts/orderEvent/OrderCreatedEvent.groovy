package contracts.orderEvent

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label 'orderCreatedForNotificationService'
    input {
        triggeredBy('orderCreatedEvent()')
    }

    outputMessage {
        sentTo('com.bhhan.example.order.domain.Order')
        body('''{"orderId": 99,"state": "PENDING"}''')
        headers {
            header('event-aggregate-type', 'com.bhhan.example.order.domain.Order')
            header('event-type', 'com.bhhan.example.order.api.events.OrderCreatedEvent')
            header('event-aggregate-id', '99')
        }
    }
}