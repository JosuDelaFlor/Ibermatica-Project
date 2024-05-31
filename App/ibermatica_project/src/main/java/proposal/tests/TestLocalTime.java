package proposal.tests;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import proposal.model.base.DataBase;

public class TestLocalTime {

    static DataBase db = new DataBase("localhost", "ibermatica_db", null, "root", null);
    public static void main(String[] args) {
        // Reservation reservation = db.searchSpecificReservation(1);

        // System.out.println(reservation.getStartDate());
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);

        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        System.out.println(localDate);

        System.out.println(timestamp.toLocalDateTime().getHour());

        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        System.out.println(localDateTime);

        LocalDate localDate2 = localDateTime.toLocalDate();
        System.out.println(localDate2);

        LocalDateTime startDate = LocalDateTime.of(2024, 05, 13, 10, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 05, 14, 8, 30, 0);

        System.out.println(startDate);
        System.out.println(endDate);

        LocalDate starDate2 = startDate.toLocalDate();
        ArrayList<LocalDate> dateList = new ArrayList<LocalDate>();

        System.out.println("\n\n------------\n\n");

        for (int i = 0; i < 5; i++) {
            LocalDate newDate = starDate2.plusDays(i);
            dateList.add(newDate);
            System.out.println(dateList.get(i));
        }
        dateTest();
    }

    public static void dateTest() {
        System.out.println("\nDateTest\n");
        String date = "2024-01-01";

        LocalDate localDate = LocalDate.parse(date);
        System.out.println(localDate);
    }
}
