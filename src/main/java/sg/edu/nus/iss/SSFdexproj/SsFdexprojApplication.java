package sg.edu.nus.iss.SSFdexproj;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.SSFdexproj.models.Pokemon;
import sg.edu.nus.iss.SSFdexproj.service.PokedexService;
import sg.edu.nus.iss.SSFdexproj.utils.Constants;

@SpringBootApplication
public class SsFdexprojApplication implements CommandLineRunner{
	@Autowired
	PokedexService pokedexService;

	public static void main(String[] args) {
		SpringApplication.run(SsFdexprojApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
		 HashMap<String, String> pokeMap = pokedexService.getUrlMap();

		 if (!pokedexService.redisKeyExists(Constants.pokedexRedisKey))
		 pokedexService.getDexData(pokeMap);

		 Pokemon pokemon = pokedexService.getRedisDexEntry("601");

		 System.out.println(pokemon.toString());

	}


}
