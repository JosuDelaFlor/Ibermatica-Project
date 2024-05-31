package demo.model;

import java.time.LocalDate;

/*
 * NO TOCAR
 */

public class Meeting {
    String name;
    int personAmount;
    LocalDate meetingDate;
    
    public Meeting(String name, int personAmount, LocalDate meetingDate) {
        this.name = name;
        this.personAmount = personAmount;
        this.meetingDate = meetingDate;
    }

    public String getName() {
        return name;
    }

    public int getPersonAmount() {
        return personAmount;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    @Override
    public String toString() {
        return " Reinuon: " +name+ ", Catidad de asistentes: " +personAmount+ ", Fecha: " + meetingDate;
    }
}
