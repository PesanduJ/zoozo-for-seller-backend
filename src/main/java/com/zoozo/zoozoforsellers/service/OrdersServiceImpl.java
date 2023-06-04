package com.zoozo.zoozoforsellers.service;

import com.zoozo.zoozoforsellers.entity.Orders;
import com.zoozo.zoozoforsellers.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService{

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public Orders addOrder(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    public Orders getOrdersById(Long id) {
        return ordersRepository.findById(id).get();
    }

    @Override
    public Orders updateOrderById(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public List<Orders> getAllOrdersBySellerId(String sellerId) {
        return ordersRepository.findAllBySellerId(sellerId);
    }

    @Override
    public void completeOrder(Long id) {
        ordersRepository.deleteById(id);
    }

}
