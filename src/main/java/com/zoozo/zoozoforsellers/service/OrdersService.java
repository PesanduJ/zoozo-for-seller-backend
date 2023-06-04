package com.zoozo.zoozoforsellers.service;


import com.zoozo.zoozoforsellers.entity.Orders;

import java.util.List;

public interface OrdersService {

    Orders addOrder(Orders orders);

    Orders getOrdersById(Long id);

    Orders updateOrderById(Orders orders);

    List<Orders> getAllOrders();

    List<Orders> getAllOrdersBySellerId(String sellerId);

    void completeOrder(Long id);
}
