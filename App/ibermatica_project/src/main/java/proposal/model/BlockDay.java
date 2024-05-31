package proposal.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BlockDay {
    private LocalDate startDay, endDate;
    private long dayDiff;
    
    public BlockDay(LocalDate startDay, LocalDate endDate, long dayDiff) {
        this.startDay = startDay;
        this.endDate = endDate;
        this.dayDiff = dayDiff;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getDayDiff() {
        return dayDiff;
    }

    public void setDayDiff(long dayDiff) {
        this.dayDiff = dayDiff;
    }

    public static long calculateDayDiff(LocalDate startDate, LocalDate endDate) {
        long dayDiff = ChronoUnit.DAYS.between(startDate, endDate);
        return dayDiff;
    }
}
