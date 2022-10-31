package com.example.mulinge.jwt;

import com.example.mulinge.jwt.entities.Product;
import com.example.mulinge.jwt.entities.Role;
import com.example.mulinge.jwt.entities.UserModel;
import com.example.mulinge.jwt.serviceimpl.ServicesImplementation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringBootMysqlJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMysqlJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner commandLineRunner(ServicesImplementation servicesImplementation){

		return args -> {
			Role roleUser = new Role( "ROLE_USER");
			Role roleAdmin = new Role( "ROLE_ADMIN");
			Role roleSuperAdmin = new Role( "ROLE_SUPER_ADMIN");

			servicesImplementation.createRole(roleUser);
			servicesImplementation.createRole(roleAdmin);
			servicesImplementation.createRole(roleSuperAdmin);

			Product prod1 = new Product(null, "Samsung A32", 10);
			Product prod2 = new Product(null, "Hp EliteBook", 50);
			Product prod3 = new Product(null, "Z Fold", 100);
			Product prod4 = new Product(null, "Lenovo Yoga", 5);
			Product prod5 = new Product(null, "Hp 250G4", 10);
			Product prod6 = new Product(null, "Hp Envy 249", 20);
			Product prod7 = new Product(null, "Samsung Galaxy Tab", 50);

			servicesImplementation.createProduct(prod1);
			servicesImplementation.createProduct(prod2);
			servicesImplementation.createProduct(prod3);
			servicesImplementation.createProduct(prod4);
			servicesImplementation.createProduct(prod5);
			servicesImplementation.createProduct(prod6);
			servicesImplementation.createProduct(prod7);

			List<Product> martinProducts = new ArrayList<>();
			martinProducts.add(prod1);
			martinProducts.add(prod2);
			martinProducts.add(prod3);

			List<Product> tarrusProducts = new ArrayList<>();
			tarrusProducts.add(prod3);
			tarrusProducts.add(prod4);

			List<Product> remaProducts = new ArrayList<>();
			remaProducts.add(prod3);
			remaProducts.add(prod4);
			remaProducts.add(prod1);
			remaProducts.add(prod2);
			remaProducts.add(prod3);

			List<Product> brownProducts = new ArrayList<>();
			brownProducts.add(prod2);
			brownProducts.add(prod7);
			brownProducts.add(prod6);
			brownProducts.add(prod3);

			List<Product> nigelProducts = new ArrayList<>();
			nigelProducts.add(prod7);

			servicesImplementation.createUser(new UserModel(null, "Chris Martin", "chrisM", "chrisM@gmail.com", "chrisM123", martinProducts, new ArrayList<>()));
			servicesImplementation.createUser(new UserModel(null, "Tarrus Riley", "tarrusR", "tarrusRM@gmail.com", "tarrusR123", tarrusProducts, new ArrayList<>()));
			servicesImplementation.createUser(new UserModel(null, "Rema Nigeria", "remaN", "remaN@gmail.com", "remaN123", remaProducts, new ArrayList<>()));
			servicesImplementation.createUser(new UserModel(null, "Chris Brown", "chrisB", "chrisB@gmail.com", "chrisB123", brownProducts, new ArrayList<>()));
			servicesImplementation.createUser(new UserModel(null, "James Nigel", "jamesN", "jamesN@gmail.com", "jamesN123", nigelProducts, new ArrayList<>()));

			servicesImplementation.addRoleToUser("chrisM", "ROLE_USER");
			servicesImplementation.addRoleToUser("tarrusR", "ROLE_SUPER_ADMIN");
			servicesImplementation.addRoleToUser("chrisM", "ROLE_ADMIN");
			servicesImplementation.addRoleToUser("chrisM", "ROLE_SUPER_ADMIN");
			servicesImplementation.addRoleToUser("remaN", "ROLE_USER");
			servicesImplementation.addRoleToUser("chrisB", "ROLE_USER");
			servicesImplementation.addRoleToUser("chrisB", "ROLE_ADMIN");
			servicesImplementation.addRoleToUser("jamesN", "ROLE_USER");
		};
	}

}
