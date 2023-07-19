package com.online.ordering.system.order.service.domain.ports.input.message.listener.shopapproval;

import com.online.ordering.system.order.service.domain.dto.message.ShopApprovalResponse;

public interface ShopApprovalResponseMessageListener {
    void orderApproved(ShopApprovalResponse shopApprovalResponse);
    void orderRejected(ShopApprovalResponse shopApprovalResponse);
}
