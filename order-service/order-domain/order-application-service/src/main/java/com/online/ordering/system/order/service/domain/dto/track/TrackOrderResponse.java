package com.online.ordering.system.order.service.domain.dto.track;

import com.online.ordering.system.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class TrackOrderResponse {

    @NonNull
    private final UUID orderTrackingId;
    @NonNull
    private final OrderStatus orderStatus;
    private final List<String> failureMessages;
}
