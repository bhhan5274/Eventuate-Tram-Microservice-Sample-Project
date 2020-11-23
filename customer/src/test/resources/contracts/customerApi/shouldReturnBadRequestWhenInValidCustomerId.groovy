package contracts.customerApi

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return badRequest response when customerId is invalid"

    request {
        method GET()
        url("/api/v1/customers/2")
    }

    response {
        headers {
            contentType('text/plain')
        }
        body("Not Found CustomerId: 2")
        status 400
    }
}