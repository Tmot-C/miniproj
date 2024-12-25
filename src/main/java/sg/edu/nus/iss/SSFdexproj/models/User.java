package sg.edu.nus.iss.SSFdexproj.models;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class User {
    
    private String uid;

    @NotEmpty(message = "Plese enter a username.")
    @Size(min = 3, max = 25, message = "Username must be between 3 and 25 characters.")
    private String username;

    @NotEmpty(message = " Plese enter an Email address is required")
    @Email(message = "Incorrect email format. lease provide a valid email address.")
    private String email;

    @NotEmpty(message = "Please enter a password")
    @Size(min = 7, message = "Password must be at least 8 characters long")
    private String password;

    private List<Pokemon> favouritePkmn;
    
    public User() {
        this.uid = UUID.randomUUID().toString().substring(0,4);
    }

    

    public User(String uid,
            @NotEmpty(message = "Plese enter a username.") @Size(min = 3, max = 25, message = "Username must be between 3 and 25 characters.") String username,
            @NotEmpty(message = " Plese enter an Email address is required") @Email(message = "Incorrect email format. lease provide a valid email address.") String email,
            @NotEmpty(message = "Please enter a password") @Size(min = 7, message = "Password must be at least 8 characters long") String password) {
        this.uid = UUID.randomUUID().toString().substring(0,4);
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public User(String uid,
            @NotEmpty(message = "Plese enter a username.") @Size(min = 3, max = 25, message = "Username must be between 3 and 25 characters.") String username,
            @NotEmpty(message = " Plese enter an Email address is required") @Email(message = "Incorrect email format. lease provide a valid email address.") String email,
            @NotEmpty(message = "Please enter a password") @Size(min = 7, message = "Password must be at least 8 characters long") String password,
            List<Pokemon> favouritePkmn) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.favouritePkmn = favouritePkmn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Pokemon> getFavouritePkmn() {
        return favouritePkmn;
    }

    public void setFavouritePkmn(List<Pokemon> favouritePkmn) {
        this.favouritePkmn = favouritePkmn;
    }



}
