package com.online.ordering.system.order.service.domain.mapper;

import com.online.ordering.system.domain.valueobject.CustomerId;
import com.online.ordering.system.domain.valueobject.Money;
import com.online.ordering.system.domain.valueobject.ProductId;
import com.online.ordering.system.domain.valueobject.ShopId;
import com.online.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.online.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.online.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.online.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.online.ordering.system.order.service.domain.entity.Order;
import com.online.ordering.system.order.service.domain.entity.OrderItem;
import com.online.ordering.system.order.service.domain.entity.Product;
import com.online.ordering.system.order.service.domain.entity.Shop;
import com.online.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Shop createOrderCommandToShop(CreateOrderCommand createOrderCommand) {
        return Shop.builder()
                .shopId(new ShopId(createOrderCommand.getShopId()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                                new Product(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList()))

                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .shopId(new ShopId(createOrderCommand.getShopId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))

                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(
            List<com.online.ordering.system.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        OrderItem.builder()
                                .product(new Product(new ProductId(orderItem.getProductId())))
                                .price(new Money(orderItem.getPrice()))
                                .quantity(orderItem.getQuantity())
                                .subTotal(new Money(orderItem.getSubTotal()))
                                .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }

}
