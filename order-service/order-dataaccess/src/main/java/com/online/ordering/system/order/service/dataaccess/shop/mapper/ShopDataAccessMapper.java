package com.online.ordering.system.order.service.dataaccess.shop.mapper;

import com.online.ordering.system.domain.valueobject.Money;
import com.online.ordering.system.domain.valueobject.ProductId;
import com.online.ordering.system.domain.valueobject.ShopId;
import com.online.ordering.system.order.service.dataaccess.shop.entity.ShopEntity;
import com.online.ordering.system.order.service.dataaccess.shop.exception.ShopDataAccessException;
import com.online.ordering.system.order.service.domain.entity.Product;
import com.online.ordering.system.order.service.domain.entity.Shop;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ShopDataAccessMapper {

    public List<UUID> shopToShopProducts(Shop shop) {
        return shop.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Shop shopEntityToShop(List<ShopEntity> shopEntities) {
        ShopEntity shopEntity =
                shopEntities.stream().findFirst().orElseThrow(() ->
                        new ShopDataAccessException("Shop could not be found!"));

        List<Product> shopProducts = shopEntities.stream().map(entity ->
                new Product(new ProductId(entity.getProductId()), entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();

        return Shop.builder()
                .shopId(new ShopId(shopEntity.getShopId()))
                .products(shopProducts)
                .active(shopEntity.getShopActive())
                .build();
    }
}
