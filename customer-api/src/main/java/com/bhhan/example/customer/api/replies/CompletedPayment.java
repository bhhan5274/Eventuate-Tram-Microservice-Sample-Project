package com.bhhan.example.customer.api.replies;

import com.bhhan.example.common.vo.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Created by hbh5274@gmail.com on 2020-11-22
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
@NoArgsConstructor
public class CompletedPayment {
    private Money saving;

    @Builder
    public CompletedPayment(Money saving){
        this.saving = saving;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedPayment that = (CompletedPayment) o;
        return Objects.equals(saving, that.saving);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saving);
    }
}
