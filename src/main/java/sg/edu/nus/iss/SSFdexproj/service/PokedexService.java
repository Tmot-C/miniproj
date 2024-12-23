package sg.edu.nus.iss.SSFdexproj.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.SSFdexproj.utils.Constants;
import sg.edu.nus.iss.SSFdexproj.utils.PokedexUtils;
import sg.edu.nus.iss.SSFdexproj.utils.Url;
import sg.edu.nus.iss.SSFdexproj.models.Pokemon;
import sg.edu.nus.iss.SSFdexproj.repo.PokedexRepo;

@Service
public class PokedexService {

    @Autowired
    PokedexRepo pokedexRepo;

    @Autowired
    private ObjectMapper objectMapper;
    RestTemplate restTemplate = new RestTemplate();

    public HashMap<String, String> getUrlMap(){

        ResponseEntity<String> pokeList = restTemplate.getForEntity(Url.pokemonList, String.class);
        JSONObject jsonobject = new JSONObject(pokeList.getBody());

        JSONArray jsonArray = jsonobject.getJSONArray("results");
        HashMap<String, String> pokeMap = new HashMap<String, String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject initialListObject = jsonArray.getJSONObject(i);
            String name = initialListObject.getString("name");
            String url = initialListObject.getString("url");
            pokeMap.put(name, url);
            
        }

        return pokeMap;

        // InputStream is = new ByteArrayInputStream(payload.getBytes());
        // JsonReader jr = Json.createReader(is);
        // JsonObject jObject = jr.readObject();
        // JsonArray jArray = jObject.getJsonArray("results");

        // System.out.println(jArray);
    }

    public void getDexData(HashMap<String, String> map) throws JsonMappingException, JsonProcessingException{

        int counter = 0;
        for (String value : map.values()) {
            
            Pokemon pokemon = new Pokemon();

            ResponseEntity<String> pokeData = restTemplate.getForEntity(value, String.class);
            JSONObject jsonObject = new JSONObject(pokeData.getBody());
            
            int id = jsonObject.getInt("id");
            String name = PokedexUtils.stringOps(jsonObject.getString("name"));
            int height = jsonObject.getInt("height");
            int weight = jsonObject.getInt("weight");

            JSONArray abilitiesArray = jsonObject.getJSONArray("abilities");
            List<String> abilities = new ArrayList<>();
            for (int i = 0; i < abilitiesArray.length(); i++) {
                JSONObject abilityObject = abilitiesArray.getJSONObject(i).getJSONObject("ability");
                abilities.add(PokedexUtils.stringOps(abilityObject.getString("name")));
            }
            
            JSONArray typesArray = jsonObject.getJSONArray("types");
            List<String> types = new ArrayList<>();
            for (int i = 0; i < typesArray.length(); i++) {
                JSONObject typeObject = typesArray.getJSONObject(i).getJSONObject("type");
                types.add(PokedexUtils.stringOps(typeObject.getString("name")));
            }

            JSONArray statsArray = jsonObject.getJSONArray("stats");
            List<Integer> stats = new ArrayList<>();
            for (int i = 0; i < statsArray.length(); i++) {
                JSONObject statObject = statsArray.getJSONObject(i);
                stats.add(statObject.getInt("base_stat"));
            }

            int bst = stats.stream().mapToInt(i -> i).sum();

            JSONArray movesArray = jsonObject.getJSONArray("moves");
            List<String> moves = new ArrayList<>();
            for (int i = 0; i < movesArray.length(); i++) {
                JSONObject moveObject = movesArray.getJSONObject(i).getJSONObject("move");
                moves.add(PokedexUtils.stringOps(moveObject.getString("name")));
                
            }

            String officialArtworkUrl = "";
            if (jsonObject.has("sprites")) {
                JSONObject spritesObject = jsonObject.getJSONObject("sprites");
                if (spritesObject.has("other")) {
                    JSONObject otherObject = spritesObject.getJSONObject("other");
                    if (otherObject.has("official-artwork")) {
                        JSONObject officialArtworkObject = otherObject.getJSONObject("official-artwork");
                        if (officialArtworkObject.has("front_default")) {
                            officialArtworkUrl = officialArtworkObject.getString("front_default");
                        }
                    }
                }
            }

            String spriteUrl = "";
            if (jsonObject.has("sprites")) {
                JSONObject spritesObject = jsonObject.getJSONObject("sprites");
                if (spritesObject.has("front_default")) {
                    spriteUrl = spritesObject.getString("front_default");
                }
            }

            pokemon.setId(id);
            pokemon.setName(name);
            pokemon.setHeight(height);
            pokemon.setWeight(weight);
            pokemon.setAbilities(abilities);
            pokemon.setTypes(types);
            pokemon.setMoves(moves);
            pokemon.setOfficial_artwork(officialArtworkUrl);
            pokemon.setSprite(spriteUrl);

            savePokemonEntry(pokemon);

            counter++;
            System.out.println(counter);

            
        }

        
    }

    public void savePokemonEntry (Pokemon pokemon) throws JsonProcessingException {
        String pkmnJsonString = objectMapper.writeValueAsString(pokemon);

        pokedexRepo.setHash(Constants.pokedexRedisKey, String.valueOf(pokemon.getName()), pkmnJsonString);
    }

    public Boolean redisKeyExists(String redisKey) {
        return pokedexRepo.hashExists(redisKey);

    }
}
