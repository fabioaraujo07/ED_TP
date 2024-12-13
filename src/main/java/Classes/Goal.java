package Classes;
import java.util.List;

public class Goal {
    private Division division;
    private String type;
    private boolean required;

    /**
     * Constructs a Goal with the specified division and type.
     *
     * @param division the division associated with the goal
     * @param type the type of the goal
     */
    public Goal(Division division, String type) {
        this.division = division;
        this.type = type;
        this.required = false;
    }

    /**
     * Returns the division associated with the goal.
     *
     * @return the division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Sets the division associated with the goal.
     *
     * @param division the new division
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    /**
     * Returns the type of the goal.
     *
     * @return the type of the goal
     */
    public String getType() {
        return type;
    }

    /**
     * Returns whether the goal is required.
     *
     * @return true if the goal is required, false otherwise
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets whether the goal is required.
     *
     * @param required the new required status
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Returns a string representation of the goal.
     *
     * @return a string representation of the goal
     */
    @Override
    public String toString() {
        return "Divisão: " + division + ", Tipo: " + type;
    }
}
