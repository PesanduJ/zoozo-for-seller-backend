package com.zoozo.zoozoforsellers.service;

import com.zoozo.zoozoforsellers.entity.Sales;

import java.time.LocalDate;
import java.util.List;

public interface SalesService {

    Sales addSoldItem(Sales sales);

    List<Sales> getSales();

    List<Sales> getAllSalesBySellerId(String sellerId);

    List<Sales> getSalesBetweenDays(LocalDate startDate, LocalDate endDate);
}
