package ai.rbs.knowledge;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private List<String> antecedents;
    private String consequent;
    private int id;
    private boolean executed;

    public Rule() {
        antecedents = new ArrayList<>();
    }
    public Rule(int id, String consequent, List<String> antecedents) {
        this.id = id;
        this.consequent = consequent;
        this.antecedents = antecedents;
    }

    public void addAntecedent(String antecedent) {
        antecedents.add(antecedent);
    }

    public List<String> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(List<String> antecedents) {
        this.antecedents = antecedents;
    }

    public String getConsequent() {
        return consequent;
    }

    public void setConsequent(String consequent) {
        this.consequent = consequent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
