package demo.model.base;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/*
 * NO TOCAR
 */

public class Validation {
    static Pattern userIdPattern = Pattern.compile("^\\d{8}[A-Z]$");
    static Pattern isNumeric = Pattern.compile("\\d");
    static Pattern numberPattern = Pattern.compile("^\\d{1,2}$");

    static ArrayList<Student> studentLis = new ArrayList<Student>();

    public static boolean checkUserId(String userId) throws IOException {
        if (!userId.replaceAll("\\s", "").isEmpty()) {
            Matcher userIdMat = userIdPattern.matcher(userId.replaceAll("\\s", ""));
            boolean validFormat = (!userIdMat.matches()) ? false : true;

            studentLis = Student.studentInitialize();
            boolean isValid = false;
            for (Student student : studentLis) {
                isValid = (student.getUserId().equals(userId.replaceAll("\\s", ""))) ? false : true;
            }

            if (!validFormat) {
                generateAlert("El formato del DNI insertado no es valido");
            } else if (!isValid) {
                generateAlert("El DNI insertado ya existe");
            }

            boolean valid = (validFormat && isValid) ? true : false;
            return valid;
        } else {
            generateAlert("El campo DNI tiene que estar completado");
            return false;
        }
    }

    public static boolean checkIsNumeric(String userName) throws IOException {
        if (!userName.replaceAll("\\s", "").isEmpty()) {
            Matcher userNameMat = isNumeric.matcher(userName.replaceAll("\\s", ""));
            boolean valid = (userNameMat.find()) ? false : true;
            if (!valid) {
                generateAlert("El nombre no puede contener valores numericos");
            } else {
                return valid;
            }
        } else {
            generateAlert("El campo del nombre tiene que estar completado");
        }
        return false;
    }

    public static boolean checkUserAge(String age) throws IOException {
        if (!age.replaceAll("\\s", "").isEmpty()) {
            Matcher userAgeMat = numberPattern.matcher(age.replaceAll("\\s", ""));
            boolean valid = (userAgeMat.matches()) ? true : false;
            if (!valid) {
                generateAlert("La edad solo puede contener valores numericos y tener como maximo 2 digitos");
            } else {
                return valid;
            }
        } else {
            generateAlert("El campo de la edad tiene que estar completado");
        }
        return false;
    }

    public static boolean checkCourse(String course) throws IOException {
        if (!course.equals(null)) {
            if (course.equals("DAM") || course.equals("DAW")) {
                return true;
            } else {
                generateAlert("Los cursos solo pueden ser DAM o DAW");
                return false;
            }
        } else {
            generateAlert("Tienes que seleccionar un curso");
            return false;
        }
    }

    public static boolean checkDate(LocalDate date) throws IOException {
        if (!date.equals(null)) {
            if (date.isAfter(LocalDate.now())) {
                generateAlert("La fecha insetada tiene que ser superior a la fecha actual");
            } else {
                return true;
            }
        } else {
            generateAlert("El campo de la feca tiene que estar completado");
        }
        return false;
    }

    /**
     * @param errorMsg
     * @throws IOException
     */
    
    @FXML
    public static void generateAlert(String errorMsg) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR!");
        alert.setHeaderText(errorMsg);
        alert.show();
    }
}
