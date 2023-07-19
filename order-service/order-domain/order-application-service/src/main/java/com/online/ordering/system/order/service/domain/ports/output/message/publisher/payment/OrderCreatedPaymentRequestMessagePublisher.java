package com.online.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.online.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.online.ordering.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {


}
