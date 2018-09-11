package ai.rbs;

import ai.rbs.knowledge.Rule;

import java.util.ArrayList;
import java.util.List;

public class JustificationModule {
    private List<String[]> justification;

    public JustificationModule () {
        justification = new ArrayList<>();
    }

    public void justify(int cycle, List<String> factBase, List<Rule> conflictSet, int resolutionRule) {
        String[] row = new String[5];
        String fact = "{ ", cSet = "{ ";
        for(String s : factBase) {
            if(factBase.get(factBase.size()-1) == s)
                fact += s + " ";
            else
                fact += s + ", ";
        }
        fact += "}";
        for(Rule r : conflictSet) {
            if(conflictSet.get(conflictSet.size()-1) == r)
                cSet += r.getId() + " ";
            else
                cSet += r.getId() + ", ";
        }
        cSet += "}";
        row[0] = String.valueOf(cycle);
        row[1] = fact;
        row[2] = cSet;
        row[3] = String.valueOf(resolutionRule);
        row[4] = "";
        justification.add(row);
    }

    public List<String[]> getJustification() {
        return justification;
    }
}
