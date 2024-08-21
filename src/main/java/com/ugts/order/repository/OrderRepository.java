package com.ugts.order.repository;

import java.util.List;

import com.ugts.order.entity.Order;
import com.ugts.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT o FROM Order o JOIN o.orderDetails od WHERE od.status = :status")
    List<Order> findOrderDetailsByOrderStatus(OrderStatus status);
}
