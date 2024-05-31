package demo.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

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

    public static Input[] LangChange(String langName, Input[] objList, String scene) throws IOException {
        Lang lang = new Lang(langName);
        
        ArrayList<String> keys = setKeys(scene);

        for (int i = 0; i < keys.size(); i++) {
            if (scene.equals("index") || scene.equals("material")) {
                objList[i].getButton().setText(lang.getProperty(keys.get(i)));
                objList[i].getLabel().setText(lang.getProperty(keys.get(i)));
            } else if (scene.equals("meeting")) {
                objList[i].getButton().setText(lang.getProperty(keys.get(i)));
                objList[i].getLabel().setText(lang.getProperty(keys.get(i)));
                objList[i].getTextField().setPromptText(lang.getProperty(keys.get(i)));
                objList[i].getDatePicker().setPromptText(lang.getProperty(keys.get(i)));
            } else {
                objList[i].getButton().setText(lang.getProperty(keys.get(i)));
                objList[i].getLabel().setText(lang.getProperty(keys.get(i)));
                objList[i].getTextField().setPromptText(lang.getProperty(keys.get(i)));
                objList[i].getComboBox().setPromptText(lang.getProperty(keys.get(i)));
                objList[i].getCheckBox().setText(lang.getProperty(keys.get(i)));
            }   
        }
        return objList;
    }

    private static ArrayList<String> setKeys(String scene) {
        ArrayList<String> keys = new ArrayList<String>();
        if (scene.equals("index")) {
            keys.addAll(Arrays.asList("btnAdd", "btnMeeting", "btnMaterial", "lblTitle"));
        } else if (scene.equals("material")) {
            keys.addAll(Arrays.asList("btnBuy", "lblTotalPriceDeco", "lblTicketDeco", "lblMaterialTitle"));    
        } else if (scene.equals("meeting")) {
            keys.addAll(Arrays.asList("btnAddMeeting", "btnDelete", "lblDate", "lblMeetingName", "lblAmount",
                "lblMeetingNameDelete", "lblMeetingTitle", "txfMeetingName", "txfDeleteInput", "dpkDate"));
        } else {
            keys.addAll(Arrays.asList("btnAddStudent", "lblUserId", "lblName", "lblAge", "lblCourse", "txfUserId",
                "txfStudentName", "comboCourse", "checkDebt"));
        }
        return keys;
    }
}
