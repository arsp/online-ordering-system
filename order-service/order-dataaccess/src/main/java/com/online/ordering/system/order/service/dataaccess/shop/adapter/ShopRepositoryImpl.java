package com.online.ordering.system.order.service.dataaccess.shop.adapter;

import com.online.ordering.system.order.service.dataaccess.shop.entity.ShopEntity;
import com.online.ordering.system.order.service.dataaccess.shop.mapper.ShopDataAccessMapper;
import com.online.ordering.system.order.service.dataaccess.shop.repository.ShopJpaRepository;
import com.online.ordering.system.order.service.domain.entity.Shop;
import com.online.ordering.system.order.service.domain.ports.output.repository.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;
    private final ShopDataAccessMapper shopDataAccessMapper;

    public ShopRepositoryImpl(ShopJpaRepository shopJpaRepository,
                              ShopDataAccessMapper shopDataAccessMapper) {
        this.shopJpaRepository = shopJpaRepository;
        this.shopDataAccessMapper = shopDataAccessMapper;
    }

    @Override
    public Optional<Shop> findShopInformation(Shop shop) {
        List<UUID> shopProducts =
                shopDataAccessMapper.shopToShopProducts(shop);
        Optional<List<ShopEntity>> shopEntities = shopJpaRepository
                .findByShopIdAndProductIdIn(shop.getId().getValue(),
                        shopProducts);
        return shopEntities.map(shopDataAccessMapper::shopEntityToShop);
    }
}
