package com.online.ordering.system.order.service.domain.entity;

import com.online.ordering.system.domain.entity.BaseEntity;
import com.online.ordering.system.domain.valueobject.Money;
import com.online.ordering.system.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productID, String name, Money price) {
        super.setId(productID);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId){
        super.setId(productId);
    }

    public void updateWithConfiremdNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
