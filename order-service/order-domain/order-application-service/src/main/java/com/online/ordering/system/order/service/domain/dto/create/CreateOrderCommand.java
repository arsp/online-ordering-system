package com.online.ordering.system.order.service.domain.dto.create;

import com.online.ordering.system.order.service.domain.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderCommand {
    @NonNull
    private final UUID customerId;
    @NonNull
    private final UUID shopId;
    @NonNull
    private final BigDecimal price;
    @NonNull
    private final List<OrderItem> items;
    @NonNull
    private final OrderAddress address;
}
