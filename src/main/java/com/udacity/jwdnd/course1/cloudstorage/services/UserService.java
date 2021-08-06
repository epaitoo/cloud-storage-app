package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    /**
     * Checks if the Username is available
     *
     * @param username the username of the user
     * @return boolean true if the user exist
     */
    public boolean isUsernameAvailable(String username) { return userMapper.getUser(username) == null; }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
//        System.out.println("userpassword: " + user.getPassword() + ", hpassword: " + hashedPassword + "\n");

        return userMapper.insert(new User(null, user.getUsername(), hashedPassword,
                encodedSalt, user.getFirstName(), user.getLastName()));

    }

    public User getUser(String username) { return userMapper.getUser(username); }


}
