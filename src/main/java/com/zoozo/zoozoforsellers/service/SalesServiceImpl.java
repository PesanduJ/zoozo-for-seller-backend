package com.zoozo.zoozoforsellers.service;

import com.zoozo.zoozoforsellers.entity.Sales;
import com.zoozo.zoozoforsellers.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService{

    @Autowired
    private SalesRepository salesRepository;

    @Override
    public Sales addSoldItem(Sales sales) {
        return salesRepository.save(sales);
    }

    @Override
    public List<Sales> getSales() {
        return salesRepository.findAll();
    }

    @Override
    public List<Sales> getAllSalesBySellerId(String sellerId) {
        return salesRepository.findAllBySellerId(sellerId);
    }

    @Override
    public List<Sales> getSalesBetweenDays(LocalDate startDate, LocalDate endDate) {
        return salesRepository.findBySoldDateBetween(startDate,endDate);
    }

}
