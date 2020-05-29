package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.*;
import hu.restumali.gamekeystore.repository.ProductRepository;
import hu.restumali.gamekeystore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ProductRepository productRepository;

    public void registerNewUser(UserDTO userDTO){
        if (!emailExists(userDTO.getEmail())){
            UserEntity user = new UserEntity();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRole(Arrays.asList(UserRoleType.Customer));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public UserEntity getUserByEmail(String email){ return userRepository.findByEmail(email);}

    private boolean emailExists(String email){
        return userRepository.findByEmail(email) != null;
    }

    public void updateUserAddress(String userName, Address address){
        UserEntity managedUser = userRepository.findByEmail(userName);
        managedUser.setAddress(address);
        userRepository.save(managedUser);
    }

    public void addOrderToUser(String userName, OrderEntity order){
        UserEntity managedUser = userRepository.findByEmail(userName);
        managedUser.getOrders().add(order);
        userRepository.save(managedUser);
    }

    public void giveAdmin(String userName){
        UserEntity managedUser = userRepository.findByEmail(userName);
        managedUser.getRole().add(UserRoleType.WebshopAdmin);
        userRepository.save(managedUser);
    }

    public void rateProduct(Long productId, String username, Integer rating) {
        UserEntity managedUser = userRepository.findByEmail(username);
        ProductEntity managedProduct = productRepository.findOneById(productId);
        managedUser.getRatedProducts().add(managedProduct.getId());
        managedProduct.getRatings().add(rating);
        userRepository.save(managedUser);
        productRepository.save(managedProduct);
    }

    public void modifyUser(UserDTO userDTO, HttpSession session) {
        UserEntity managedUser = userRepository.findByEmail(userDTO.getEmail());
        managedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        managedUser.setFirstName(userDTO.getFirstName());
        managedUser.setLastName(userDTO.getLastName());
        session.setAttribute("firstName", managedUser.getFirstName());
        session.setAttribute("lastName", managedUser.getLastName());
        userRepository.save(managedUser);
    }

    public void closeAccount(String username) {
        UserEntity managedUser = userRepository.findByEmail(username);
        managedUser.setEnabled(false);
        userRepository.save(managedUser);
    }
}
