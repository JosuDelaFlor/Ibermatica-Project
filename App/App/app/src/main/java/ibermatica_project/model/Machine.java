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

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
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
