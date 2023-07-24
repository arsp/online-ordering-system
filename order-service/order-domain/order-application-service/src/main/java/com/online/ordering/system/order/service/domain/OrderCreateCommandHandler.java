package com.online.ordering.system.order.service.domain;

import com.online.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.online.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.online.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.online.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.online.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;


    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper,
                                     OrderDataMapper orderDataMapper, OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}",orderCreatedEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());

    }


}
