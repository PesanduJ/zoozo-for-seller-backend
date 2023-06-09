package com.zoozo.zoozoforsellers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoozo.zoozoforsellers.entity.Orders;
import com.zoozo.zoozoforsellers.entity.Product;
import com.zoozo.zoozoforsellers.entity.Sales;
import com.zoozo.zoozoforsellers.entity.User;
import com.zoozo.zoozoforsellers.repository.ProductRepository;
import com.zoozo.zoozoforsellers.service.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private ImageService imageService;


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
    public ResponseEntity<String> addNewProduct(@RequestParam("file") MultipartFile file, @RequestParam("product") String productJson) throws IOException {

        try{
            // Parse the productJson string into a Product object using a JSON parser
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);

            // Handle the file upload and get the image key
            String imageKey = imageService.saveImage(file);

            // Update the Product object with the image key
            product.setImageKey(imageKey);

            // Save the product
            productService.addProduct(product);

            return ResponseEntity.ok("Product created successfully");

        } catch (IOException e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save product");
        }

    }

    @GetMapping("/product/{productCode}")
    public ResponseEntity<Product> getProductById(@PathVariable String productCode){
        Product product = productService.getProductById(productCode);
        if(product != null){
            String imageKey = product.getImageKey();
            String imageUrl = "https://zoozoo-product-images.s3.ap-south-1.amazonaws.com/" + imageKey;
            product.setImageKey(imageUrl);
            return ResponseEntity.ok(product);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allProducts")
    public List<Product> getAllProducts(){
        List<Product> products = productService.getAllProducts();

        // Generate image URLs for each product
        for (Product product : products) {
            String imageKey = product.getImageKey();
            String imageUrl = "https://zoozoo-product-images.s3.ap-south-1.amazonaws.com/" + imageKey;
            product.setImageKey(imageUrl);
        }

        return products;
    }

    @PutMapping("/product/{productCode}")
    public Product updateProductById(@PathVariable String productCode, @RequestBody Product product){
        Product productUpdated = productService.getProductById(productCode);

        productUpdated.setProductName(product.getProductName());
        productUpdated.setDescription(product.getDescription());
        productUpdated.setSellingPrice(product.getSellingPrice());
        productUpdated.setInStock(product.getInStock());
        productUpdated.setProductValue(product.getProductValue());

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

    @PutMapping("/order/tracking/{id}")
    public Orders updatedTrackingDetailsById(@PathVariable Long id, @RequestParam String trackingDetails){
        Orders ordersUpdated = ordersService.getOrdersById(id);
        ordersUpdated.setTrackingDetails(trackingDetails);

        return ordersService.updateOrderById(ordersUpdated);
    }

    @GetMapping("/allOrders")
    public List<Orders> getAllOrders(){
        return ordersService.getAllOrders();
    }


    @GetMapping("/order/{id}")
    public Orders getOrderById(@PathVariable Long id){
        return ordersService.getOrdersById(id);
    }


    @DeleteMapping("/order/{id}")
    public void removeOrderById(@PathVariable Long id){
        ordersService.removeOrder(id);
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
        sales.setCommission(selectedOrder.getCommission());
        sales.setSoldDate(LocalDate.now());
        sales.setOrderId(selectedOrder.getId());

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
