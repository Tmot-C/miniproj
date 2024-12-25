package sg.edu.nus.iss.SSFdexproj.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.SSFdexproj.models.User;
import sg.edu.nus.iss.SSFdexproj.repo.LoginRepo.UserRepo;
import sg.edu.nus.iss.SSFdexproj.utils.Constants;

public class LoginService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;


    public void regNewUser(User user) throws JsonProcessingException {

        String userString = objectMapper.writeValueAsString(user);
        
        // Store the serialized user in Redis under the category
        userRepo.setHash(Constants.userbaseRedisKey,user.getUsername().trim(), userString);
    }

    public Boolean userExists(String username) {
        System.out.println(username.trim());
        return userRepo.hasKey(Constants.userbaseRedisKey, username.trim());

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
            List<Object> usersRaw = userRepo.getAllValuesFromHash(Constants.userbaseRedisKey);

            List<User> users = new ArrayList<>();

            for (Object userObj : usersRaw) {
                String userString = (String) userObj;
                User user = objectMapper.readValue(userString, User.class);
                users.add(user);
            }

            return users;

    }

    public User getDataFromUsername(String username) throws JsonMappingException, JsonProcessingException {
        String userString = userRepo.getValueFromHash(Constants.userbaseRedisKey,username.trim());
        User user = objectMapper.readValue(userString, User.class);
        return user;
    }

    public Boolean userAuthenticate(String username, String password) throws JsonProcessingException {
        String userString= userRepo.getValueFromHash(Constants.userbaseRedisKey,username.trim());
        User user = objectMapper.readValue(userString, User.class);


        return (password == user.getPassword());
    }

    
}
