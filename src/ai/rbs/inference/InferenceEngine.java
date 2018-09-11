package ai.rbs.inference;

import ai.rbs.JustificationModule;
import ai.rbs.knowledge.KnowledgeBase;
import ai.rbs.knowledge.Rule;

import java.util.ArrayList;
import java.util.List;

public class InferenceEngine {
    private List<Rule> conflictSet;
    private JustificationModule justificationModule;
    protected List<Integer> executedRules;
    protected String newFacts;
    protected int i;

    public boolean forwardChaining(List<String> factBase, KnowledgeBase kBase, RETENetwork reteNetwork, JustificationModule justificationModule) {
        boolean success = false, goalInFactBase = false;
        newFacts = "none";
        conflictSet = new ArrayList<>();
        i = 0;
        if(executedRules == null)
            executedRules = new ArrayList<>();
        else if (executedRules.size() > 0)
            executedRules.clear();

        conflictSet.add(kBase.getRandomRule());
        for(String s : kBase.getTargetFact()) {
            if(factBase.contains(s)) {
                goalInFactBase = true;
                break;
            }
        }
        while(!goalInFactBase && !conflictSet.isEmpty()) {
            i++;
            if(newFacts.equals("none"))
                for (String fact : factBase) {
                    reteNetwork.propagate(fact);
                }
            else
                reteNetwork.propagate(newFacts);
            conflictSet = reteNetwork.getSuccessfulRules();
            if(!conflictSet.isEmpty()) {
                update(resolve('f'), factBase, justificationModule);
            }
            for(String s : kBase.getTargetFact()) {
                if(factBase.contains(s)) {
                    goalInFactBase = true;
                    break;
                }
            }
        }
        goalInFactBase = false;
        for(String s : kBase.getTargetFact()) {
            if(factBase.contains(s)) {
                goalInFactBase = true;
                break;
            }
        }
        if (goalInFactBase)
            success = true;
        return success;
    }

    public boolean backwardChainning (String goal, List<String> factBase, RETENetwork reteNetwork) {
        if(verify(goal, factBase, reteNetwork))
            return true;
        else
            return false;
    }

    private Rule resolve (char chainingType) {
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
                }
            }
            for (Rule r : conflictSet) {
                if(r.getAntecedents().size() == aux)
                    selectedRules.add(r);
            }
            aux = 0;
            if (selectedRules.size() > 1) {
                for (Rule r : selectedRules) {
                    if (aux < r.getId()) {
                        aux = r.getId();
                        selectedRule = r;
                    }
                }
                //conflictSet.remove(selectedRule);
                return selectedRule;
            } else {
                //conflictSet.remove(selectedRules.get(0));
                if(!selectedRules.isEmpty())
                    return selectedRules.get(0);
                else return null;
            }
        } else {
            //Resolve for forwardChaining
            return null;
        }
    }

    private void update (Rule r, List<String> factBase, JustificationModule justificationModule) {
        if(r != null) {
            executedRules.add(r.getId());
            newFacts = r.getConsequent();
            justificationModule.justify(i, factBase, conflictSet, r.getId());
            factBase.add(newFacts);
        }
    }

    private boolean verify (String goal, List<String> factBase, RETENetwork reteNetwork) {
        conflictSet = new ArrayList<>();
        List<String> newGoals;
        boolean verified = false;
        if(factBase.contains(goal))
            return true;
        else {
            // Conflict set = Equate(Consequent(KB), Goal)
            for(Node n : reteNetwork.getBetaNodes()) {
                if(n.getEquateRule().getConsequent().equals(goal)) {
                    conflictSet.add(n.getEquateRule());
                }
            }
            while(!conflictSet.isEmpty() && !verified) {
                Rule r = resolve('f');
                conflictSet.remove(r);
                newGoals = r.getAntecedents();
                verified = true;
                while(!newGoals.isEmpty() && verified) {
                    goal = newGoals.get(0);
                    newGoals.remove(goal);
                    verified = verify(goal,factBase, reteNetwork);
                    if(verified)
                        factBase.add(goal);
                }
            }
            return verified;
        }
    }

}
