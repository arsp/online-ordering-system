package com.online.ordering.system.order.service.dataaccess.shop.repository;

import com.online.ordering.system.order.service.dataaccess.shop.entity.ShopEntity;
import com.online.ordering.system.order.service.dataaccess.shop.entity.ShopEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopJpaRepository extends JpaRepository<ShopEntity, ShopEntityId> {

    Optional<List<ShopEntity>> findByShopIdAndProductIdIn(UUID shopId, List<UUID> productIds);
}
