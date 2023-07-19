package com.online.ordering.system.order.service.domain.dto.message;

import com.online.ordering.system.domain.valueobject.OrderApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ShopApprovalResponse {
    private String id;
    private String sagaId;
    private String orderId;
    private String shopId;
    private Instant createdAt;
    private OrderApprovalStatus orderApprovalStatus;
    private List<String> failureMessages;
}
