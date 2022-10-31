package com.example.mulinge.jwt.serviceimpl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.mulinge.jwt.entities.Product;
import com.example.mulinge.jwt.entities.Role;
import com.example.mulinge.jwt.entities.UserModel;
import com.example.mulinge.jwt.service.ProductService;
import com.example.mulinge.jwt.service.RoleService;
import com.example.mulinge.jwt.service.UserService;
import com.example.mulinge.jwt.utils.JWTConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@Transactional
public class ServicesImplementation implements UserDetailsService {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(ServicesImplementation.class);

    public UserModel createUser(UserModel user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userService.save(user);
    }
    public List<UserModel> getUsers(){
        return userService.findAll();
    }

    public UserModel getUser(String username){
        return userService.findByUsername(username);
    }

    public Role createRole(Role role){
        return roleService.save(role);
    }
    public Role getRole(String name){
        return roleService.findByName(name);
    }

    public void addRoleToUser(String username, String role){
        UserModel user = userService.findByUsername(username);
        Role r = roleService.findByName(role);

        if(user == null){
            throw new IllegalArgumentException("Username not found");
        }

        user.getRoles().add(r);
    }
    public List<Role> getUserRoles(String userEmail) {
        UserModel user = userService.findByEmail(userEmail);
        return user.getRoles();
    }

    public Product createProduct(Product product){
        return productService.save(product);
    }

    public List<Product> getProducts(){
        return productService.findAll();
    }

    public Product getProduct(String name){
        Product prod = productService.findByName(name);

        if(prod == null){
            throw new IllegalArgumentException("Product not found");
        }

        return prod;
    }
    public List<Product> getProductsByUser(String username){
       UserModel user= userService.findByUsername(username);

        if(user == null){
            throw new IllegalArgumentException("Username not found");
        }

        return user.getProducts();
    }
    public void addProductToUser(String username, Product product){
        UserModel user = userService.findByUsername(username);

        if(user == null){
            throw new IllegalArgumentException("Username not found");
        }

        user.getProducts().add(product);
    }

    public Map<String, String> refreshRoute(String refreshToken){

        //verify the token
        try{
            DecodedJWT decodedJWT = JWTConfig.verifyAccessToken(refreshToken);

            //username
            String username = decodedJWT.getSubject();

            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

            //generate new access token
            String newToken = JWTConfig.createAccessToken(username, stream(roles).map(String::valueOf).collect(Collectors.toList()), new Date(System.currentTimeMillis() + 10 * 60 * 1000));

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", newToken);
            tokens.put("refresh_token", refreshToken);

            return tokens;
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException(exception.getMessage(), exception.getCause());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userService.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("User email not found");
        }

        logger.info("Found user: {}", user);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new User(user.getEmail(), user.getPassword(), authorities);
    }

}
