package ibermatica_project.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javafx.scene.control.Label;

public class Lang extends Properties {
    private static final long serialVersionUID = 1L;

    public Lang(String lang) {
        switch (lang) {
            case "Spanish":
                getProperties("spanish.properties");
                break;
            case "English":
            default:
                getProperties("english.properties");
                break;
        }
    }

    private void getProperties(String lang) {
        try {
            this.load(getClass().getResourceAsStream(lang));
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @SuppressWarnings("exports")
    public static Label[] langChange(String langName, Label[] labelList, String scene) throws IOException{
        ArrayList<String> key = new ArrayList<String>();
        if (scene.equals("admMenu")) {
            key.addAll(Arrays.asList("lblTitle", "lblName", "lblSurname", "lblUserName", "lblEmail", "lblPhoneNumber", "lblUserType"));
        }

        Lang lang = new Lang(langName);

        for (int i = 0; i < key.size(); i++) {
            labelList[i].setText(lang.getProperty(key.get(i)));
        }
        return labelList;
    }
}
