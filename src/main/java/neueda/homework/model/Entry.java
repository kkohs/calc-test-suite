package neueda.homework.model;

/**
 * Class representing single test entry.
 *
 * @author Kristaps Kohs
 */
public class Entry {
    /** Entry name. */
    private String name;
    /** Variable A. */
    private String variableA;
    /** Variable B.. */
    private String variableB;
    /** Expected result. */
    private String result;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariableA() {
        return variableA;
    }

    public void setVariableA(String variableA) {
        this.variableA = variableA;
    }

    public String getVariableB() {
        return variableB;
    }

    public void setVariableB(String variableB) {
        this.variableB = variableB;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
