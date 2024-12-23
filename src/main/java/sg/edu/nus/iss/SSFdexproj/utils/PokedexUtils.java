package sg.edu.nus.iss.SSFdexproj.utils;

import org.apache.commons.lang3.text.WordUtils;

public class PokedexUtils {
    public static String stringOps(String string) {

        string = string.replace("-", " ");
        string = WordUtils.capitalize(string);
        
        return string;
    }
}
