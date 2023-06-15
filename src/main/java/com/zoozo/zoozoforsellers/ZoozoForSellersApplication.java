package com.zoozo.zoozoforsellers;

import com.zoozo.zoozoforsellers.entity.User;
import com.zoozo.zoozoforsellers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin
@ComponentScan
public class ZoozoForSellersApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/message")
	public String message(){
		return "Zoozo For Sellers!!!";
	}

//
	@GetMapping("/checkUserRole")
	public String checkUserRole(@RequestParam("username") String username, @RequestParam("password") String password) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			if (passwordEncoder.matches(password, user.getPassword())) {
				if ("ADMIN".equals(user.getRole())) {
					return "ADMIN";
				} else if ("USER".equals(user.getRole())) {
					return "USER";
				}
			}
		}
		return "Invalid";
	}

	public static void main(String[] args) {
		SpringApplication.run(ZoozoForSellersApplication.class, args);
	}

}
