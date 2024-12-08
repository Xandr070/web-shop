package com.example.clothingstore.repository;

import com.example.clothingstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Month;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Найти все OrderItem для указанных месяцев.
     *
     * @param months список месяцев
     * @return список OrderItem
     */
    @Query("SELECT oi FROM OrderItem oi WHERE MONTH(oi.order.orderDate) IN :months")
    List<OrderItem> findAllByOrderDateInMonths(List<Month> months);
}
