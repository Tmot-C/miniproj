package sg.edu.nus.iss.SSFdexproj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.iss.SSFdexproj.models.Pokemon;
import sg.edu.nus.iss.SSFdexproj.models.User;
import sg.edu.nus.iss.SSFdexproj.service.PokedexService;


@Controller
public class PokedexController {
    
    @Autowired
    PokedexService pokedexService;

    @GetMapping("/")
    public String getHomePage(Model model, HttpSession session) {

        User currentLoginUser = (User) session.getAttribute("currentLoginUser");
        model.addAttribute("currentLoginUser", currentLoginUser);
        
        return "home";
    }

    @GetMapping("/pokedex")
    public String dexHome(Model model, HttpSession session) {
        
        User currentLoginUser = (User) session.getAttribute("currentLoginUser");
        model.addAttribute("currentLoginUser", currentLoginUser);
        
        return "dexHome";
    }

    @GetMapping("/pokedex/{generation}")
    public String dexOverview(@PathVariable String generation, Model model,HttpSession session) throws JsonMappingException, JsonProcessingException {
        
        User currentLoginUser = (User) session.getAttribute("currentLoginUser");
        model.addAttribute("currentLoginUser", currentLoginUser);
        model.addAttribute("genlist", pokedexService.getGen(generation));
        return "genoverview";
    }

    @GetMapping("/pokedex/{id}")
    public String dexEntry(@PathVariable String id, Model model,HttpSession session) throws JsonMappingException, JsonProcessingException{
        
        User currentLoginUser = (User) session.getAttribute("currentLoginUser");
        model.addAttribute("currentLoginUser", currentLoginUser);
        Pokemon pokemon = pokedexService.getRedisDexEntry(id);

        if (pokemon != null){
            return "invalidPokemon";
        }

        model.addAttribute("pokemon", pokemon);
        
        return"dexentry";
    }

    

    
    

}
