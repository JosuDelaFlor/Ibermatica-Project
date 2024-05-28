package demo.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javafx.scene.control.Button;

public class Lang extends Properties {
    private static final long serialVersionUID = 1L;
    
    public Lang(String lang) {
        switch (lang) {
            case "Spanish":
                getProperties("spanish.properties");
                break;
            case "Portuguese":
                getProperties("portuguese.properties");
            default:
                getProperties("portuguese.properties");
                break;
        }
    }

    private void getProperties(String lang) {
        try {
            this.load(getClass().getResourceAsStream(lang));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @SuppressWarnings("exports")
    public static Button[] buttonLangChange(String langName, Button[] buttonList, String scene) throws IOException {
        Lang lang = new Lang(langName);
        
        ArrayList<String> keys = setKeys(scene);
        for (int i = 0; i < keys.size(); i++) {
            buttonList[i].setText(lang.getProperty(keys.get(i)));
        }
        return buttonList;
    }

    private static ArrayList<String> setKeys(String scene) {
        ArrayList<String> keys = new ArrayList<String>();
        if (scene.equals("index")) {
            keys.addAll(Arrays.asList("btnAdd", "btnMeeting", "btnMaterial"));
        }
        return keys;
    }
}
