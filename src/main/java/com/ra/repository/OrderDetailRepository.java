package com.ra.repository;

import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.OrderDetailId;
import com.ra.model.entity.Orders;
import com.ra.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    @Query("select od from OrderDetail od where od.orders.id = :orderId")
    List<OrderDetail> findByOrderId(Long orderId);
}
