package sg.edu.nus.iss.SSFdexproj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.SSFdexproj.models.User;
import sg.edu.nus.iss.SSFdexproj.service.LoginService;

@Controller
public class LoginController {
    
    @Autowired
    LoginService loginService;

    @GetMapping("/login")
    public String getLogin(HttpSession session,Model model) {
        model.addAttribute("currentLoginUser",session.getAttribute("currentLoginUser"));
        if (session.getAttribute("currentLoginUser")!= null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/login/authenticate")
    public String authenticate(@RequestBody MultiValueMap<String,String> loginForm,Model model,HttpSession session) throws JsonProcessingException {
        String username = loginForm.getFirst("username");
        String password = loginForm.getFirst("password");

        //check to see if username exists
        if (!loginService.userExists(username)) {
            model.addAttribute("errorMessage","User does not exist");
            return "login";
        }
        //verify password if username exists
        if (!loginService.userAuthenticate(username, password)) {
            model.addAttribute("errorMessage","Invalid password");
            return "login";
        }

        User currentLoginUser = loginService.getDataFromUsername(username);
        session.setAttribute("currentLoginUser",currentLoginUser);
        //add session
        // String redirectUrl = (String) session.getAttribute("redirectUrl");
        // session.removeAttribute("redirectUrl"); // Clean up after redirect
        // return "redirect:" + (redirectUrl != null ? redirectUrl : "/");
        return "redirect:/";
    }
    
    @GetMapping("/registration")
    public String getRegistration(Model model,HttpSession session) {
        User user = new User();
        model.addAttribute("currentLoginUser",session.getAttribute("currentLoginUser"));
        if (session.getAttribute("currentLoginUser")!= null) {
            return "redirect:/";
        }
        model.addAttribute("user",user);
        return "registration";
    }

    @PostMapping("/registration/verify")
    public String validateRegistration(@Valid @ModelAttribute("user") User user,BindingResult result, Model model) throws JsonProcessingException {
        if (result.hasErrors()) {
            return "registration";
        }
        if (loginService.userExists(user.getUsername())) {
            model.addAttribute("errorMessage","Username already in use.");
            return "registration";
        }

        if (loginService.emailExists(user.getEmail())) {
            model.addAttribute("errorMessage","Email already in use.");
            return "registration";

        }
        
        loginService.regNewUser(user);
        return "redirect:/";
    }


}
