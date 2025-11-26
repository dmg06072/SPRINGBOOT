package com.example.demo.Domain.Common.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderId implements Serializable {
    private Long orderId;
    private Long productId;
}
