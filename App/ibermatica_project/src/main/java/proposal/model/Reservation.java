package proposal.model;

import java.time.LocalDate;

public class Reservation {
    String userId, serialNumber;
    LocalDate startDate, endDate;
    int reservationId;
    
    public Reservation(String userId, String serialNumber, LocalDate startDate, LocalDate endDate, int reservationId) {
        this.userId = userId;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getReservationId() {
        return reservationId;
    }
}
