package com.online.ordering.system.order.service;

import com.online.ordering.system.order.service.domain.OrderDomainService;
import com.online.ordering.system.order.service.domain.OrderDomainServiceImpl;
import com.online.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.online.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.online.ordering.system.order.service.domain.ports.output.message.publisher.shopapproval.OrderPaidShopRequestMessagePublisher;
import com.online.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.online.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.online.ordering.system.order.service.domain.ports.output.repository.ShopRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.online.ordering.system"})
public class OrderTestConfiguration {

    @Bean
    public OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCreatedPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCancelledPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderPaidShopRequestMessagePublisher orderPaidShopRequestMessagePublisher() {
        return Mockito.mock(OrderPaidShopRequestMessagePublisher.class);
    }

    @Bean
    public OrderRepository orderRepository(){
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public ShopRepository shopRepository(){
        return Mockito.mock(ShopRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository(){
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public OrderDomainService orderDomainService(){
        return new OrderDomainServiceImpl();
    }


}
