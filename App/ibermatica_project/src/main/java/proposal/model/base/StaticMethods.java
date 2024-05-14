package proposal.model.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import proposal.model.Validation;

/*
 * This file will be used to create static methods that will be used in the 
 * different controllers
 */

public class StaticMethods {
    static Pattern passPattern = Pattern.compile("[?!¡@¿.,´)]"); // Check special characters
    static Pattern userIdPattern = Pattern.compile("^(?=.*?[A-Z]).{1,9}$"); // Check if it contains a capital letter at the end and if it is composed of numbers
    static Pattern isNumeric = Pattern.compile("\\d");
    static Pattern tlfNumberPattern = Pattern.compile("^\\d{1,9}$");
    static Pattern emailPattern = Pattern.compile("@+");

    DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    
    public static Validation userIdValidation(String userId) {
        userId.replaceAll("\\s", "");
        Validation validation = new Validation(false, null);
        Matcher userIdMat = userIdPattern.matcher(userId);
        boolean valid = (!userIdMat.matches()) ? false : true;

        validation.setValid(valid);
        validation.setErrorMsg("El DNI no cumple con las caracteristicas");

        return validation;
    }

    public static Validation nameSurnameUsernameValidation(String name, String surname, String username) {
        username.replaceAll("\\s", "");
        Validation validation = new Validation(false, null);
        
        
        Matcher nameMat = isNumeric.matcher(name);
        Matcher surnameMat = isNumeric.matcher(surname);
        Matcher usernameMat = isNumeric.matcher(username);
        
        if (name.equals(surname) || name.equals(username) || username.equals(surname)) {
            validation.setErrorMsg("El nombre de usuario, el nombre y el apellido no puedes coincidir o estar vacios");
        } else if (name.equals("") || surname.equals("") || username.equals("")) {
            validation.setErrorMsg("El nombre, apellido y nombre de usuario tienen que estar completados");
        } else if (nameMat.find()) {
            validation.setErrorMsg("El nombre no puede contener valores numericos");
        } else if (surnameMat.find()) {
            validation.setErrorMsg("El apellido no puede contener valores numericos");
        } else if (usernameMat.find()) {
            validation.setErrorMsg("El nombre de usuario no puede contener valores numericos");
        } else {
            validation.setValid(true);
        }
        
        return validation;
    }

    public static Validation tlfNumberValidation(String tlfNumber) {
        tlfNumber.replaceAll("\\s", "");
        Validation validation = new Validation(false, null);

        Matcher tlfNumberMat = tlfNumberPattern.matcher(tlfNumber);

        boolean valid = false;
        valid = (tlfNumberMat.matches()) ? false : true;

        if (valid) {
            validation.setErrorMsg("El numero de telefono solo puede tener un maximo de 9 digitos, solo valores numericos y estar completo");
        } else {
            validation.setValid(true);
        }

        return validation;
    }

    public static Validation emailValidation(String email) {
        email.replaceAll("\\s", "");
        Validation validation = new Validation(false, null);
        
        Matcher emailMat = emailPattern.matcher(email);

        boolean valid = false;
        valid = (emailMat.find()) ? false : true;

        if (valid) {
            validation.setErrorMsg("El correo tiene que contener una '@'");
        } else if (email.equals("")) {
            validation.setErrorMsg("El correo tiene que estar completado");
        } else {
            validation.setValid(true);
        }

        return validation;
    }

    public static Validation passValidation(String pass1, String pass2) {
        pass1.replaceAll("\\s", "");
        pass2.replaceAll("\\s", "");
        Validation validation = new Validation(false, null);

        if (pass1.length() > 8) {
            boolean upper = false, number = false, lettersOrSymbol = false, special = false;

            Matcher passMat = passPattern.matcher(pass1);
    
            int i;
            char l;
    
            for (i = 0; i < pass1.length(); i++) {
                l = pass1.charAt(i);
    
                if (Character.isDigit(l)) {
                    number = true;
                }
                if (Character.isLetter(l)) {
                    lettersOrSymbol = true;
                }
                if (Character.isUpperCase(l)) { 
                    upper = true;
                }
                if (passMat.find()) {      
                    special = true;
                }
            }
    
            if (number == false || upper == false || lettersOrSymbol == false || special == false) {
                validation.setValid(false);
                validation.setErrorMsg("La contraseña tiene que contener mayusculas, minusculas y caracteres especiales");
            } else if (!pass1.equals(pass2)) {
                validation.setErrorMsg("La contraseña y la contraseña de validacion tienen que coincidir");
            } else {
                validation.setValid(true);
            }
        } else {
            validation.setErrorMsg("La contraseña tiene que contener mas de 8 caracteres");
        }

        return validation;
    }
}
