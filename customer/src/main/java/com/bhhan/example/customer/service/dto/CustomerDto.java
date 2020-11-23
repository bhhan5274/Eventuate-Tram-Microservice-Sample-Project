package com.bhhan.example.customer.service.dto;

import com.bhhan.example.common.vo.Money;
import com.bhhan.example.customer.domain.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by hbh5274@gmail.com on 2020-11-19
 * Github : http://github.com/bhhan5274
 */
public class CustomerDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CustomerRequest {
        private String name;
        private Money saving;

        @Builder
        public CustomerRequest(String name, Money saving){
            this.name = name;
            this.saving = saving;
        }

        public Customer toCustomer(){
            return Customer.builder()
                    .name(name)
                    .saving(saving)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CustomerResponse {
        private Long id;
        private String name;
        private Money saving;

        public CustomerResponse(Customer customer){
            this.id = customer.getId();
            this.name = customer.getName();
            this.saving = customer.getSaving();
        }

        public static CustomerResponse create(Customer customer){
            return new CustomerResponse(customer);
        }
    }
}
