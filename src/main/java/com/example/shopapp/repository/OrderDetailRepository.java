package com.example.shopapp.repository;

import com.example.shopapp.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
//    @Query(nativeQuery = true,value = "select o from OrderDetail o where o.order.id = :order_id")
//    List<OrderDetail> findAllByOrOrderId(@Param("order_id") Long orderId);
    List<OrderDetail> findAllByOrderId(Long orderId);
}
