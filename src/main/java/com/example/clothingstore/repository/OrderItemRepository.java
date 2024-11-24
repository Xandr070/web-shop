package com.example.clothingstore.repository;

import com.example.clothingstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query(value = "SELECT COUNT(oi) FROM OrderItem oi WHERE YEAR(oi.order.orderDate) = :year AND MONTH(oi.order.orderDate) = :month", nativeQuery = true)
    List<OrderItem> countProductsBySeason(@Param("year") int year, @Param("month") int month);


    @Query("SELECT oi.product, SUM(oi.quantity) as totalSales " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE EXTRACT(MONTH FROM o.orderDate) IN :seasonMonths " +
            "GROUP BY oi.product " +
            "ORDER BY totalSales DESC")
    List<Object[]> findTopProductsBySeason(@Param("seasonMonths") List<Integer> seasonMonths);





    List<OrderItem> findByProductId(Long productId);
}
