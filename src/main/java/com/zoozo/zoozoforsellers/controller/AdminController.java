package com.zoozo.zoozoforsellers.controller;

import com.zoozo.zoozoforsellers.entity.Orders;
import com.zoozo.zoozoforsellers.entity.Product;
import com.zoozo.zoozoforsellers.entity.Sales;
import com.zoozo.zoozoforsellers.entity.User;
import com.zoozo.zoozoforsellers.repository.ProductRepository;
import com.zoozo.zoozoforsellers.service.OrdersService;
import com.zoozo.zoozoforsellers.service.ProductService;
import com.zoozo.zoozoforsellers.service.SalesService;
import com.zoozo.zoozoforsellers.service.UserService;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private SalesService salesService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    //ADMIN FUNCTIONS
    @PostMapping("/addAdmin")
    public User addNewAdmin(@RequestBody User user){
        String password = user.getPassword();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setRole("ADMIN");
        return userService.addUser(user);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/allAdmins")
    public List<User> getAllAdmins(){
        return userService.getAllAdmins();
    }

    @GetMapping("/user/{nic}")
    public User getUserById(@PathVariable String nic){
        return userService.getUserById(nic);
    }

    @DeleteMapping("/remove/user/{nic}")
    public Boolean removeUserById(@PathVariable String nic){
        userService.removeUser(nic);
        return true;
    }

    @PutMapping("/ban/user/{nic}")
    public User banUserById(@PathVariable String nic){
        User user = userService.getUserById(nic);
        user.setRole("BANNED");
        return userService.banUser(user);
    }



    //PRODUCT FUNCTIONS
    @PostMapping("/addProduct")
    public Product addNewProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping("/product/{productCode}")
    public Product getProductById(@PathVariable String productCode){
        return productService.getProductById(productCode);
    }

    @GetMapping("/allProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PutMapping("/product/{productCode}")
    public Product updateProductById(@PathVariable String productCode, @RequestBody Product product){
        Product productUpdated = productService.getProductById(productCode);

        productUpdated.setProductName(product.getProductName());
        productUpdated.setDescription(product.getDescription());
        productUpdated.setSellingPrice(product.getSellingPrice());
        productUpdated.setInStock(product.getInStock());
        productUpdated.setProductValue(product.getProductValue());
        productUpdated.setCommission(product.getCommission());

        return productService.updateProductById(productUpdated);
    }

    @DeleteMapping("/product/{productCode}")
    public void removeProduct(@PathVariable String productCode){
        productService.removeProduct(productCode);
    }



    //ORDERS FUNCTIONS
    @PutMapping("/order/{id}")
    public Orders updateOrderById(@PathVariable Long id, @RequestParam String status){
        Orders ordersUpdated = ordersService.getOrdersById(id);

        ordersUpdated.setStatus(status);

        return ordersService.updateOrderById(ordersUpdated);
    }

    @GetMapping("/allOrders")
    public List<Orders> getAllOrders(){
        return ordersService.getAllOrders();
    }



    //SALES FUNCTIONS
    @PostMapping("/addSale/{id}")
    public Sales addSale(@PathVariable Long id){
        Orders selectedOrder = ordersService.getOrdersById(id);

        //get order details
        Sales sales = new Sales();
        sales.setProductName(selectedOrder.getProductName());
        sales.setProductCode(selectedOrder.getProductCode());
        sales.setSellingPrice(selectedOrder.getSellingPrice());
        sales.setStatus("COMPLETED");
        sales.setQuantity(selectedOrder.getQuantity());
        sales.setSellerId(selectedOrder.getSellerId());
        sales.setSoldDate(LocalDate.now());

        //removing order from order table
        ordersService.completeOrder(id);

        return salesService.addSoldItem(sales);
    }



    //TOTAL SALES
    @GetMapping("/getSales")
    public List<Sales> getAllSales(){
        return salesService.getSales();
    }

    @GetMapping("/totalSales")
    public float getTotalSales(){
        List<Sales> salesList = salesService.getSales();
        float totalSales = 0;
        for (Sales sale : salesList) {
            totalSales += sale.getQuantity() * sale.getSellingPrice();
        }
        return Float.parseFloat(String.format("%.2f", totalSales));
    }



    //WEEKLY SALES
    @GetMapping("/getWeeklySales")
    public List<Sales> getWeeklySales(){
        // Find the end date of the week (Sunday)
        LocalDate weekStartDate = LocalDate.now();
        LocalDate weekEndDate = weekStartDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        return salesService.getSalesBetweenDays(weekStartDate, weekEndDate);
    }

    @GetMapping("/weeklySales")
    public float getWeeklyTotalSales() {
        // Find the end date of the week (Sunday)
        LocalDate weekStartDate = LocalDate.now();
        LocalDate weekEndDate = weekStartDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        // Query the sales records for the week
        List<Sales> salesList = salesService.getSalesBetweenDays(weekStartDate, weekEndDate);

        // Calculate the total sales for the week
        float totalSales = 0;
        for (Sales sale : salesList) {
            totalSales += sale.getQuantity() * sale.getSellingPrice();
        }

        return Float.parseFloat(String.format("%.2f", totalSales));
    }



    //CUSTOM SALES
    @GetMapping("/getCustomSales")
    public List<Sales> getCustomSales(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return salesService.getSalesBetweenDays(startDate, endDate);
    }

    @GetMapping("/customSales")
    public float getWeeklyTotalSales(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Query the sales records for the week
        List<Sales> salesList = salesService.getSalesBetweenDays(startDate, endDate);

        // Calculate the total sales for the week
        float totalSales = 0;
        for (Sales sale : salesList) {
            totalSales += sale.getQuantity() * sale.getSellingPrice();
        }

        return Float.parseFloat(String.format("%.2f", totalSales));
    }
}
