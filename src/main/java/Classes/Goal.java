package Classes;
import java.util.List;

public class Goal {
    private Division division;
    private String type;

    public Goal(Division division, String type) {
        this.division = division;
        this.type = type;
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

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Divisão: " + division + ", Tipo: " + type;
    }

}
