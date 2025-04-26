package com.incon.backend.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // Use SuperBuilder to work with inheritance
@EqualsAndHashCode(callSuper = true)
public class BuyerResponse extends UserResponse {
    private Integer cartId;
    private List<Integer> orderIds;
}