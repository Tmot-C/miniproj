package sg.edu.nus.iss.SSFdexproj.models;
import java.util.List;

public class Pokemon {
    
    private int id;
    private String name;
    private int height;
    private int weight;

    private List<String> abilities;
    private List<String> types;
    private List<Integer> stats;
    private List<String> moves;

    private String official_artwork;
    private String sprite;
    

    public Pokemon() {
    }


    public Pokemon(int id, String name, int height, int weight, List<String> abilities, List<String> types,
            List<Integer> stats, List<String> moves, String official_artwork, String sprite) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
        this.types = types;
        this.stats = stats;
        this.moves = moves;
        this.official_artwork = official_artwork;
        this.sprite = sprite;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getHeight() {
        return height;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public int getWeight() {
        return weight;
    }


    public void setWeight(int weight) {
        this.weight = weight;
    }


    public List<String> getAbilities() {
        return abilities;
    }


    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }


    public List<String> getTypes() {
        return types;
    }


    public void setTypes(List<String> types) {
        this.types = types;
    }


    public List<Integer> getStats() {
        return stats;
    }


    public void setStats(List<Integer> stats) {
        this.stats = stats;
    }


    public List<String> getMoves() {
        return moves;
    }


    public void setMoves(List<String> moves) {
        this.moves = moves;
    }


    public String getOfficial_artwork() {
        return official_artwork;
    }


    public void setOfficial_artwork(String official_artwork) {
        this.official_artwork = official_artwork;
    }


    public String getSprite() {
        return sprite;
    }


    public void setSprite(String sprite) {
        this.sprite = sprite;
    }


    @Override
    public String toString() {
        return "Pokemon [id=" + id + ", name=" + name + ", height=" + height + ", weight=" + weight + ", abilities="
                + abilities + ", types=" + types + ", stats=" + stats + ", moves=" + moves + ", official_artwork="
                + official_artwork + ", sprite=" + sprite + "]";
    }

    

    
}
