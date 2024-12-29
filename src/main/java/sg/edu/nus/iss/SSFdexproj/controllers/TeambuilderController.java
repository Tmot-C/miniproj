package sg.edu.nus.iss.SSFdexproj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import sg.edu.nus.iss.SSFdexproj.models.Pokemon;
import sg.edu.nus.iss.SSFdexproj.models.User;
import sg.edu.nus.iss.SSFdexproj.service.PokedexService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class TeambuilderController {

    @Autowired
    PokedexService pokedexService;

    @PostMapping("/teambuilder")
    public String addToTeam(Model model, @RequestParam String id, HttpSession session) throws JsonMappingException, JsonProcessingException {
        
        User currentLoginUser = (User) session.getAttribute("currentLoginUser");
        model.addAttribute("currentLoginUser", currentLoginUser);

        String username= currentLoginUser.getUsername();
        if(!pokedexService.teamExists(username)){
            
            Pokemon pokemon = pokedexService.getRedisDexEntry(id);
            pokedexService.newTeam(pokemon, username);

            return "redirect:/teambuilder";
        }

       int teamsize = pokedexService.getTeamSize(username);

       if (teamsize == 6) {
        
        String fullMessage = "You cannot have a team with more than 6 Pokemon, remove at least one to add more.";
        session.setAttribute("fullMessage", fullMessage);

        return "redirect:/teambuilder";
       }
       
       Pokemon pokemon = pokedexService.getRedisDexEntry(id);
       pokedexService.addToTeam(pokemon, username);
        return "redirect:/teambuilder";
    }

    @GetMapping("/teambuilder")
    public String getTeam(Model model,  HttpSession session) throws JsonMappingException, JsonProcessingException {
        
        User currentLoginUser = (User) session.getAttribute("currentLoginUser");
        model.addAttribute("currentLoginUser", currentLoginUser);
        String username= currentLoginUser.getUsername();

        String fullMessage = (String) session.getAttribute("fullMessage");
        model.addAttribute("fullMessage", fullMessage);
        //reset Full notification if it was triggered
        session.setAttribute("fullMessage", null);
        try {
            List<Pokemon> team = pokedexService.getTeam(username);
            model.addAttribute("team", team);
            if (team.size() == 0) {
                return "emptyteam";
            }
           
            return "teambuilder";
        } catch (Exception e) {
            return"emptyteam";
        }
     
    }
    
    @PostMapping("/teambuilder/remove")
    public String removeFromTeam(Model model, @RequestParam String id, HttpSession session) throws JsonMappingException, JsonProcessingException{
        
        User currentLoginUser = (User) session.getAttribute("currentLoginUser");
        model.addAttribute("currentLoginUser", currentLoginUser);
        String username= currentLoginUser.getUsername();

        int pokeId = Integer.parseInt(id);

        pokedexService.removeFromTeam(pokeId, username);

        return "redirect:/teambuilder";
    }

    
}
