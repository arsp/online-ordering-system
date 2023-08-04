package com.online.ordering.system.order.service.messaging.mapper;

import com.online.ordering.system.kafka.order.avro.model.*;
import com.online.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.online.ordering.system.order.service.domain.dto.message.ShopApprovalResponse;
import com.online.ordering.system.order.service.domain.entity.Order;
import com.online.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.online.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.online.ordering.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent){
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent){
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public ShopApprovalRequestAvroModel orderPaidEventToShopApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent){
        Order order = orderPaidEvent.getOrder();
        return ShopApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setOrderId(order.getId().getValue())
                .setShopId(order.getShopId().getValue())
                .setShopStatus(ShopOrderStatus
                        .valueOf(order.getStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem ->
                        Product.newBuilder()
                                .setId(orderItem.getProduct().getId().getValue().toString())
                                .setQuantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setShopStatus(ShopOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel){
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId().toString())
                .sagaId(paymentResponseAvroModel.getSagaId().toString())
                .paymentId(paymentResponseAvroModel.getPaymentId().toString())
                .customerId(paymentResponseAvroModel.getCustomerId().toString())
                .orderId(paymentResponseAvroModel.getOrderId().toString())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(com.online.ordering.system.domain.valueobject.PaymentStatus.valueOf(
                        paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public ShopApprovalResponse
    approvalResponseAvroModelToApprovalResponse(ShopApprovalResponseAvroModel
                                                        shopApprovalResponseAvroModel) {
        return ShopApprovalResponse.builder()
                .id(shopApprovalResponseAvroModel.getId().toString())
                .sagaId(shopApprovalResponseAvroModel.getSagaId().toString())
                .shopId(shopApprovalResponseAvroModel.getShopId().toString())
                .orderId(shopApprovalResponseAvroModel.getOrderId().toString())
                .createdAt(shopApprovalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(com.online.ordering.system.domain.valueobject.OrderApprovalStatus.valueOf(
                        shopApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(shopApprovalResponseAvroModel.getFailureMessages())
                .build();
    }

}
