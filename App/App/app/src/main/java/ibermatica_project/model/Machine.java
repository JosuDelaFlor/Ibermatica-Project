package ibermatica_project.model;

import java.time.LocalDate;

public class Machine {
    String serialNumber, name;
    LocalDate acquisitionDate;
    String type, status;

    public Machine(String serialNumber, String name, LocalDate acquisitionDate, String type, String status) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.acquisitionDate = acquisitionDate;
        this.type = type;
        this.status = status;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getName() {
        return name;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}
