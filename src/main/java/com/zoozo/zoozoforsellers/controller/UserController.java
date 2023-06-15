package com.zoozo.zoozoforsellers.controller;

import com.zoozo.zoozoforsellers.config.CustomUserDetails;
import com.zoozo.zoozoforsellers.config.CustomUserDetailsService;
import com.zoozo.zoozoforsellers.entity.Orders;
import com.zoozo.zoozoforsellers.entity.Product;
import com.zoozo.zoozoforsellers.entity.Sales;
import com.zoozo.zoozoforsellers.entity.User;
import com.zoozo.zoozoforsellers.service.OrdersService;
import com.zoozo.zoozoforsellers.service.ProductService;
import com.zoozo.zoozoforsellers.service.SalesService;
import com.zoozo.zoozoforsellers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

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
    private UserDetailsService userDetailsService;


    //USER FUNCTIONS
    @PostMapping("/addUser")
    public User addNewUser(@RequestBody User user){
        String password = user.getPassword();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setRole("USER");
        return userService.addUser(user);
    }

    @GetMapping("/user/{nic}")
    public User getUserById(@PathVariable String nic){
        return userService.getUserById(nic);
    }

    @PutMapping("/updateUser")
    public User updateUserById(@RequestBody User user){

        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User loggedUser =  ((CustomUserDetails) userDetails).getUser();

        //setting updated values
        User userProfile = userService.getUserById(loggedUser.getNic());

        userProfile.setUsername(user.getUsername());

            //encrypting password
            String password = user.getPassword();
            String encryptedPassword = bCryptPasswordEncoder.encode(password);
            userProfile.setPassword(encryptedPassword);

        userProfile.setMobileNo(user.getMobileNo());
        userProfile.setBankDetails(user.getBankDetails());

        return userService.updateUserById(userProfile);
    }

    //ORDERS FUNCTIONS
    @PostMapping("/addOrder/{productCode}")
    public Orders addOrder(@RequestParam int quantity,@RequestParam float commission, @PathVariable String productCode, @RequestParam String customerDetails){

        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user =  ((CustomUserDetails) userDetails).getUser();

        //get product by id
        Product product = productService.getProductById(productCode);

        //setting the order
        Orders orders = new Orders();
        orders.setProductCode(product.getProductCode());
        orders.setProductName(product.getProductName());
        orders.setSellingPrice(product.getSellingPrice());
        orders.setStatus("PENDING");
        orders.setQuantity(quantity);
        orders.setSellerId(user.getNic());
        orders.setCommission(commission);
        orders.setOrderDate(LocalDate.now());
        orders.setCustomerDetails(customerDetails);
        orders.setTrackingDetails("No Data Available Yet!");

        return ordersService.addOrder(orders);
    }

    @GetMapping("/allOrders")
    public List<Orders> getAllOrdersBySellerId(){
        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user =  ((CustomUserDetails) userDetails).getUser();

        return ordersService.getAllOrdersBySellerId(user.getNic());
    }



    //SALES FUNCTIONS
    @GetMapping("/salesBySellerId")
    public List<Sales> getAllSalesBySellerId(){

        //get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user =  ((CustomUserDetails) userDetails).getUser();

        return salesService.getAllSalesBySellerId(user.getNic());
    }

    @GetMapping("/pointsBySeller")
    public float getPointsBySeller(@RequestParam String productCode){
        return productService.getProductValueByProductCode(productCode);
    }
}
