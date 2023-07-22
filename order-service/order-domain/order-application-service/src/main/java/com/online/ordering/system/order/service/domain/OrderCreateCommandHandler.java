package com.online.ordering.system.order.service.domain;

import com.online.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.online.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.online.ordering.system.order.service.domain.dto.create.OrderItem;
import com.online.ordering.system.order.service.domain.entity.Customer;
import com.online.ordering.system.order.service.domain.entity.Order;
import com.online.ordering.system.order.service.domain.entity.Shop;
import com.online.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.online.ordering.system.order.service.domain.exception.OrderDomainException;
import com.online.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.online.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.online.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.online.ordering.system.order.service.domain.ports.output.repository.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     ShopRepository shopRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.orderDataMapper = orderDataMapper;
    }


    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Shop shop = checkShop(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, shop);
        Order orderResult = saveOrder(order);
        log.info("Order is created with id: {}",orderResult.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(order);

    }

    private Shop checkShop(CreateOrderCommand createOrderCommand) {
        Shop shop = orderDataMapper.createOrderCommandToShop(createOrderCommand);
        Optional<Shop> optionalShop = shopRepository.findShopInformation(shop);
        if (optionalShop.isEmpty()) {
            log.warn("Could not find the Shop with shop id: {}", createOrderCommand.getShopId());
            throw new OrderDomainException("Could not find the Shop with shop id: " + createOrderCommand.getShopId());
        }
        return optionalShop.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomers(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id: " + customerId);
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if(orderResult == null){
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}",orderResult.getId().getValue());
        return orderResult;

    }

}
