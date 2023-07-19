package com.online.ordering.system.order.service.domain.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class TrackOrderQuery {
    @NonNull
    private final UUID orderTrackingId;
}
