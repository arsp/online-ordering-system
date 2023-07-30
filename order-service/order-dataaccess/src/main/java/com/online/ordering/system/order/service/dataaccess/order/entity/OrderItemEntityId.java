package com.online.ordering.system.order.service.dataaccess.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntityId implements Serializable {

    private Long id;
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntityId that = (OrderItemEntityId) o;
        return id.equals(that.id) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
