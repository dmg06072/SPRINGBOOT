package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.Order;
import com.example.demo.Domain.Common.Entity.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId> {
}
