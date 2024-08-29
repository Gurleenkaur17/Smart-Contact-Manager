package com.scm.service.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstant;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public User saveUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstant.ROLE_USER));
        userRepo.save(user);
        return user;
    }

    @Override
    public Optional<User> getUserById(String id) {
        Optional<User> user = userRepo.findById(id);
        return user != null ? user : null;
    }

    @Override
    public User updateUser(User user) {
        User userFound = userRepo.findById(user.getId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        userFound.setName(user.getName());
        userFound.setEmail(user.getEmail());
        userFound.setAbout(user.getAbout());
        userFound.setConatct(user.getConatct());
        userFound.setPassword(user.getPassword());
        userRepo.save(userFound);
        return userFound; 
    }
 
    @Override
    public void deleteUser(String id) {
        User userFound = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        userRepo.delete(userFound);
    }

    @Override
    public boolean isUserExist(String id) {
        User user = userRepo.findById(id).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        
        return userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }
    
}
