package com.gscdz.service;

import com.gscdz.model.User;
import com.gscdz.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepo;
    public User findByUsername(String username){
        Optional<User> optUser=userRepo.findByUsername(username);
        if(optUser.isEmpty()){
            log.info("USER NOT EXISTS");
            return null;
        }
        return optUser.get();
    }

}
