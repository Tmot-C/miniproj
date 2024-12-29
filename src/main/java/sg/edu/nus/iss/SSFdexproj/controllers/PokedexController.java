package sg.edu.nus.iss.SSFdexproj.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        
        return "dexhome";
    }

    @GetMapping("/pokedex/gen/{generation}")
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
        int i;
        try {
            i = Integer.parseInt(id);
         }
         catch (NumberFormatException e) {
            return "error";
         }

        if (i > 1025 || i < 1) {
            return "error";
        }

        Pokemon pokemon = pokedexService.getRedisDexEntry(id);
        model.addAttribute("pokemon", pokemon);

        Map<String, Integer> statsWithNames = new LinkedHashMap<>();
        statsWithNames.put("HP", pokemon.getStats().get(0));
        statsWithNames.put("Attack", pokemon.getStats().get(1));
        statsWithNames.put("Defense", pokemon.getStats().get(2));
        statsWithNames.put("Special Attack", pokemon.getStats().get(3));
        statsWithNames.put("Special Defense", pokemon.getStats().get(4));
        statsWithNames.put("Speed", pokemon.getStats().get(5));

        model.addAttribute("statsWithNames", statsWithNames);
        
        
        return"dexentry";
    }




}   
