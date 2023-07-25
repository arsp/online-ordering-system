package com.online.ordering.system.order.service.domain;

import com.online.ordering.system.order.service.domain.dto.message.ShopApprovalResponse;
import com.online.ordering.system.order.service.domain.ports.input.message.listener.shopapproval.ShopApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class ShopApprovalResponseMessageListenerImpl implements ShopApprovalResponseMessageListener {
    @Override
    public void orderApproved(ShopApprovalResponse shopApprovalResponse) {

    }

    @Override
    public void orderRejected(ShopApprovalResponse shopApprovalResponse) {

    }
}
