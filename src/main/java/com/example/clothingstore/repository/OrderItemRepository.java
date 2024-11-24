package com.example.clothingstore.repository;

import com.example.clothingstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi.product, SUM(oi.quantity) AS total FROM OrderItem oi " +
            "JOIN oi.order o WHERE EXTRACT(MONTH FROM o.orderDate) IN :months " +
            "GROUP BY oi.product ORDER BY total DESC")
    List<Object[]> findTopProductsBySeason(@Param("months") List<Integer> months);
}
