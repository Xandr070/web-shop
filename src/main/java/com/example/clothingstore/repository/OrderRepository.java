package com.example.clothingstore.repository;

import com.example.clothingstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT SUM(o.total) FROM Order o WHERE MONTH(o.orderDate) IN :months")
    Double calculateTotalSalesByMonths(@Param("months") List<Integer> months);
}
