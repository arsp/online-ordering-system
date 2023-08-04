package com.online.ordering.system.order.service;

import com.online.ordering.system.domain.valueobject.*;
import com.online.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.online.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.online.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.online.ordering.system.order.service.domain.dto.create.OrderItem;
import com.online.ordering.system.order.service.domain.entity.Customer;
import com.online.ordering.system.order.service.domain.entity.Order;
import com.online.ordering.system.order.service.domain.entity.Product;
import com.online.ordering.system.order.service.domain.entity.Shop;
import com.online.ordering.system.order.service.domain.exception.OrderDomainException;
import com.online.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.online.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.online.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.online.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.online.ordering.system.order.service.domain.ports.output.repository.ShopRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private OrderDataMapper orderDataMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("445e6c84-2ba6-11ee-be56-0242ac120002");
    private final UUID SHOP_ID = UUID.fromString("66ec99e2-2ba6-11ee-be56-0242ac120002");
    private final UUID PRODUCT_ID = UUID.fromString("7786ded4-2ba6-11ee-be56-0242ac120002");
    private final UUID ORDER_ID = UUID.fromString("8b867c0a-2ba6-11ee-be56-0242ac120002");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .shopId(SHOP_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .shopId(SHOP_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("250.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .shopId(SHOP_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("210.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));


        Shop shopResponse = Shop.builder()
                .shopId(new ShopId(createOrderCommand.getShopId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomers(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(shopRepository.findShopInformation(orderDataMapper.createOrderCommandToShop(createOrderCommand)))
                .thenReturn(Optional.of(shopResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    public void createOrder() {
        CreateOrderResponse createOrderResponse  = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
        assertEquals("Order created successfully!", createOrderResponse.getMessage());
        assertNotNull(createOrderResponse.getOrderTrackingId());

    }

    @Test
    public void testCreateOrderWithWrongTotalPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertEquals("Total price: 250.00 is not equal to Order items total: 200.00!", orderDomainException.getMessage());
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertEquals("Order item price: 60.00 is not valid for product " + PRODUCT_ID, orderDomainException.getMessage());
    }

    @Test
    public void testCreateOrderWithPassiveShop() {
        Shop shopResponse = Shop.builder()
                .shopId(new ShopId(createOrderCommand.getShopId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))))
                .active(false)
                .build();
        when(shopRepository.findShopInformation(orderDataMapper.createOrderCommandToShop(createOrderCommand)))
                .thenReturn(Optional.of(shopResponse));
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));
        assertEquals("Shop with id: " + SHOP_ID + " is currently not active!", orderDomainException.getMessage());
    }



}
