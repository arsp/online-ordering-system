package com.online.ordering.system.order.service.dataaccess.customer.respository;

import com.online.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.online.ordering.system.order.service.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}
