package com.online.ordering.system.order.service.domain.ports.output.repository;

import com.online.ordering.system.order.service.domain.entity.Shop;

import java.util.Optional;

public interface ShopRepository {

    Optional<Shop> findShopInformation(Shop shop);
}
