package contracts.messaging

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return payment fail"

    label 'payOrderChargeFail'
    input {
        messageFrom('customerService')
        messageBody('''{"orderId": 2, "orderTotal": {"amount": 2000}, "customerId": 1}''')
        messageHeaders {
            header('command_type', 'com.bhhan.example.customer.api.commands.ReserveCreditCommand')
            header('command_saga_type', 'com.bhhan.example.order.saga.CreateOrderSaga')
            header('command_saga_id', $(consumer(regex('[0-9a-f]{16}-[0-9a-f]{16}'))))
            header('command_reply_to', 'com.bhhan.example.order.saga.CreateOrderSaga-reply')
        }
    }

    outputMessage {
        sentTo('com.bhhan.example.order.saga.CreateOrderSaga-reply')

        headers {
            header('reply_type', 'com.bhhan.example.customer.api.replies.CustomerCreditLimitExceeded')
            header('reply_outcome-type', 'FAILURE')
        }
    }
}