package sg.edu.nus.iss.SSFdexproj.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import sg.edu.nus.iss.SSFdexproj.models.Pokemon;
import sg.edu.nus.iss.SSFdexproj.service.PokedexService;

@RestController
public class PokedexRestController {

    @Autowired
    PokedexService pokedexService;

    @GetMapping("/pokedex/{id}.json")
    public Pokemon getPokemonAPI(@PathVariable String id) throws JsonMappingException, JsonProcessingException{
        
        int i;
        try {
            i = Integer.parseInt(id);
         }
         catch (NumberFormatException e) {
            return null;
         }

        if (i > 1025 || i < 1) {
            return null;
        }

        Pokemon pokemon = pokedexService.getRedisDexEntry(id);
        return pokemon;
    }
        
}
