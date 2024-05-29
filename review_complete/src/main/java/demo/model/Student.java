package demo.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import demo.model.base.User;
import demo.model.base.Validation;

/*
 * Students va a ser una clase extendida de User
 *  - 1. Se le agregaran dos atributos al objeto (String course(DAM o DAW) y boolean debt[por defecto sera false])
 *  - 2. Se creara un metodo donde crearas 3 Estudiantes que podras utilizarlo en cualquier clase (studentInitialize)
 *  - * Puedes crear getters/setters
 */

public class Student extends User {
    private String course;
    private boolean debt = false;

    public Student(String userId, String name, int age, String course, boolean debt) {
        super(userId, name, age);
        this.course = course;
        this.debt = debt;
    }

    protected void setCourse(String course) {
        boolean valid = (course.equals("DAM") || course.equals("DAW")) ? true : false;
        if (valid) {
            this.course = course;
        } else {
            try {
                Validation.generateAlert("El curso insertado no es correcto (DAM o DAW)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Student> studentInitialize() {
        ArrayList<Student> studentList = new ArrayList<Student>();
        studentList.addAll(Arrays.asList(new Student("45659241D", "Josu", 19, "DAM", false),
            new Student("23235530X", "Maider", 18, "DAW", false),
            new Student("12578470T", "Aner", 20, "DAM", true)));
        return studentList;
    }

    public String getCourse() {
        return course;
    }

    public boolean isDebt() {
        return debt;
    }

    @Override
    public String toString() {
        return "Estudiante: " + userId + " Nombre: " + name + " Edad: " + age + " Curso: " + course
                + ", Deuda: " + (!debt ? "Pagada" : "Por pagar");
    }

    
}
