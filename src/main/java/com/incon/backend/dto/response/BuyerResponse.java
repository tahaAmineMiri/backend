package com.incon.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BuyerResponse extends UserResponse {
    private Integer cartId;
    private List<Integer> orderIds;
    private List<Integer> reviewIds;
    private Integer subscriptionId;
}