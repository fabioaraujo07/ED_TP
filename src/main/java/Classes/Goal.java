package Classes;
import java.util.List;

public class Goal {
    private Division division;
    private String type;
    private boolean required;

    public Goal(Division division, String type) {
        this.division = division;
        this.type = type;
        this.required = false;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public String getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "Divisão: " + division + ", Tipo: " + type;
    }

}
