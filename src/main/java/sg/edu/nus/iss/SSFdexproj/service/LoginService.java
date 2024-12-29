package sg.edu.nus.iss.SSFdexproj.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.SSFdexproj.models.User;
import sg.edu.nus.iss.SSFdexproj.repo.LoginRepo;

import sg.edu.nus.iss.SSFdexproj.utils.Constants;

@Service
public class LoginService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    LoginRepo loginRepo;

    @Autowired
    private ObjectMapper objectMapper;


    public void regNewUser(User user) throws JsonProcessingException {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String userString = objectMapper.writeValueAsString(user);
        
        // Store the serialized user in Redis under the category
        loginRepo.setHash(Constants.userbaseRedisKey,user.getUsername().trim(), userString);
    }

    public Boolean userExists(String username) {
        return loginRepo.hasKey(Constants.userbaseRedisKey, username.trim());

    }

    public Boolean emailExists(String email) throws JsonMappingException, JsonProcessingException {
        
        List<User> users = this.getUserList();
        
        for (User user : users) {
            if (email.toLowerCase().equals(user.getEmail().toLowerCase())) {
                return true;
            }
        }
        return false;

    }

    public List<User> getUserList() throws JsonMappingException, JsonProcessingException {
            List<Object> usersRaw = loginRepo.getAllValuesFromHash(Constants.userbaseRedisKey);

            List<User> users = new ArrayList<>();

            for (Object userObj : usersRaw) {
                String userString = (String) userObj;
                User user = objectMapper.readValue(userString, User.class);
                users.add(user);
            }

            return users;

    }

    public User getDataFromUsername(String username) throws JsonMappingException, JsonProcessingException {
        String userString = loginRepo.getValueFromHash(Constants.userbaseRedisKey,username.trim());
        User user = objectMapper.readValue(userString, User.class);
        return user;
    }

    public Boolean userAuthenticate(String username, String password) throws JsonProcessingException {
        String userString= loginRepo.getValueFromHash(Constants.userbaseRedisKey,username.trim());
        User user = objectMapper.readValue(userString, User.class);


        return passwordEncoder.matches(password, user.getPassword());
    }

    
}
