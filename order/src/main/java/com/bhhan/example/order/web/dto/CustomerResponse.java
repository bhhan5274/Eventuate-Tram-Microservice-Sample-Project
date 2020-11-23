package com.bhhan.example.order.web.dto;

import com.bhhan.example.common.vo.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by hbh5274@gmail.com on 2020-11-19
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
@NoArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private Money saving;
}
