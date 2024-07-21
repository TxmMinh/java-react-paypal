package com.alibou.payment;

import lombok.Data;

@Data
public class RequestDto {
    String method;
    String amount;
    String currency;
    String description;
}
