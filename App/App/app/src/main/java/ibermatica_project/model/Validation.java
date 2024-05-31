package ibermatica_project.model;

public class Validation {
    boolean valid = false;
    String errorMsg;
    
    public Validation(boolean valid, String errorMsg) {
        this.valid = valid;
        this.errorMsg = errorMsg;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
