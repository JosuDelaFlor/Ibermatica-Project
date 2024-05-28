package ibermatica_project.lang;

import java.io.IOException;
import java.util.Properties;

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
}
