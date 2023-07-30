package com.online.ordering.system.order.service.dataaccess.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopEntityId implements Serializable {

    private UUID shopId;
    private UUID productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopEntityId that = (ShopEntityId) o;
        return shopId.equals(that.shopId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopId, productId);
    }
}
