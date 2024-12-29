package sg.edu.nus.iss.SSFdexproj;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import sg.edu.nus.iss.SSFdexproj.models.Pokemon;
import sg.edu.nus.iss.SSFdexproj.service.PokedexService;
import sg.edu.nus.iss.SSFdexproj.utils.Constants;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SsFdexprojApplication implements CommandLineRunner{
	@Autowired
	PokedexService pokedexService;

	public static void main(String[] args) {
		SpringApplication.run(SsFdexprojApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
		 HashMap<String, String> pokeMap = pokedexService.getUrlMap();

		 if (!pokedexService.redisKeyExists(Constants.pokedexRedisKey)){
			pokedexService.getDexData(pokeMap);
			System.out.println("success");
		 }
		 

		

	}


}
