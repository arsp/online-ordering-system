package com.online.ordering.system.order.service.domain.ports.output.repository;

import com.online.ordering.system.order.service.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findCustomers(UUID uuid);
}
