package com.online.ordering.system.order.service.domain.ports.output.message.publisher.shopapproval;

import com.online.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.online.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidShopRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
