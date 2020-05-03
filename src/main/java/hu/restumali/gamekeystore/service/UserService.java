package hu.restumali.gamekeystore.service;

import hu.restumali.gamekeystore.model.Address;
import hu.restumali.gamekeystore.model.UserDTO;
import hu.restumali.gamekeystore.model.UserEntity;
import hu.restumali.gamekeystore.model.UserRoleType;
import hu.restumali.gamekeystore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void registerNewUser(UserDTO userDTO){
        if (!emailExists(userDTO.getEmail())){
            UserEntity user = new UserEntity();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRole(UserRoleType.Customer);
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
}
