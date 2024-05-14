package proposal.model.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import proposal.model.Validation;

/*
 * This file will be used to create static methods that will be used in the 
 * different controllers
 */

public class StaticMethods {
    static Pattern passPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    static Pattern userIdPattern = Pattern.compile("^(?=.*?[A-Z]).{1,9}$");
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

        Matcher passMat = passPattern.matcher(pass1);

        if (!pass1.equals(pass2)) {
            validation.setErrorMsg("La contraseña y la contraseña de validacion tienen que ser iguales");
        } else if (passMat.matches()) {
            validation.setErrorMsg("La contraseña tiene que contener minimo una mayuscula, un caracter especial y 8 digitos");
        } else {
            validation.setValid(true);
        }

        return validation;
    }
}
