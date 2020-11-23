package contracts.customerApi

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return customer dto response when customerId is valid"

    request {
        method GET()
        url("/api/v1/customers/1")
    }

    response {
        headers {
            contentType('application/json')
        }

        body("{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"bhhan\",\n" +
                "  \"saving\": {\n" +
                "    \"amount\": 1000\n" +
                "  }\n" +
                "}")
        status 200
    }
}