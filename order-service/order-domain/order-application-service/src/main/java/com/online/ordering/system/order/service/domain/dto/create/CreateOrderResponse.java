package com.online.ordering.system.order.service.domain.dto.create;

import com.online.ordering.system.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class CreateOrderResponse {

    @NonNull
    private final UUID orderTrackingId;
    @NonNull
    private final OrderStatus orderStatus;
    @NonNull
    private final String message;

}
