package com.example.clothingstore.repository;

import com.example.clothingstore.entity.OrderItem;
import com.example.clothingstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi.product, SUM(oi.quantity) AS totalSales " +
            "FROM OrderItem oi JOIN oi.order o " +
            "WHERE EXTRACT(MONTH FROM o.orderDate) IN :seasonMonths " +
            "GROUP BY oi.product " +
            "ORDER BY totalSales DESC")
    List<Object[]> findTopProductsBySeason(List<Integer> seasonMonths);
}
