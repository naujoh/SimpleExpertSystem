package main.java.ai.rbs.inference;

import main.java.ai.rbs.knowledge.KnowledgeBase;
import main.java.ai.rbs.knowledge.Rule;
import java.util.ArrayList;
import java.util.List;

public class InferenceEngine {
    private List<Rule> conflictSet;
    protected List<Integer> executedRules;
    protected String newFacts;

    public boolean forwardChainning (List<String> factBase, KnowledgeBase kBase, RETENetwork reteNetwork, String goal) {
        boolean success = false;
        newFacts = "none";
        conflictSet = new ArrayList<>();

        if(executedRules == null)
            executedRules = new ArrayList<>();
        else if (executedRules.size() > 0)
            executedRules.clear();

        conflictSet.add(kBase.getRandomRule());
        while(!factBase.contains(goal) && !conflictSet.isEmpty()) {
            if(newFacts.equals("none"))
                for (String fact : factBase) {
                    reteNetwork.propagate(fact);
                }
            else
                reteNetwork.propagate(newFacts);
            conflictSet = reteNetwork.getSuccessfulRules();
            if(!conflictSet.isEmpty()) {
                update(resolve('f'), factBase);
            }
        }
        if (factBase.contains(goal))
            success = true;
        return success;
    }

    public boolean backwardChainning (String goal, List<String> factBase) {
        if(verify(goal, factBase))
            return true;
        else
            return false;
    }

    public Rule resolve (char chainingType) {
        if(chainingType == 'f') {
            List<Rule> selectedRules = new ArrayList<>();
            Rule selectedRule = null;
            int aux = 0;
            //Remove from conflict set the executed rules
            List<Rule> toRemove = new ArrayList<>();
            for (Rule r : conflictSet) {
                if (executedRules.contains(r.getId()))
                    toRemove.add(r);
            }
            conflictSet.removeAll(toRemove);
            //Resolve a rule from conflict set
            for (Rule r : conflictSet) {
                if (aux <= r.getAntecedents().size()) {
                    aux = r.getAntecedents().size();
                    selectedRules.add(r);
                }
            }
            aux = 0;
            if (selectedRules.size() > 1) {
                for (Rule r : selectedRules) {
                    if (aux < r.getId()) {
                        aux = r.getId();
                        selectedRule = r;
                    }
                }
                conflictSet.remove(selectedRule);
                return selectedRule;
            } else {
                conflictSet.remove(selectedRules.get(0));
                return selectedRules.get(0);
            }
        } else {
            //Resolve for forwardChaining
            return null;
        }
    }

    public void update (Rule r, List<String> factBase) {
        executedRules.add(r.getId());
        newFacts = r.getConsequent();
        factBase.add(newFacts);
    }

    public boolean verify (String goal, List<String> factBase) {
        boolean verified = false;
        if(factBase.contains(goal))
            return true;
        else {

        }
        return verified;
    }

}
