package ai.rbs.inference;

import ai.rbs.knowledge.Rule;

import java.util.ArrayList;
import java.util.List;

public class RETENetwork {
    private Node root;
    private List<Rule> successfulRules;
    private List<Node> betaNodes;

    public RETENetwork() {
        successfulRules = new ArrayList<>();
        betaNodes = new ArrayList<>();
    }

    public void create(List<Rule> rules) {
        if(!betaNodes.isEmpty())
            betaNodes.clear();
        root = new Node("root");
        Node classNode, alphaNode;
        for (Rule r : rules) {
            Node betaNode = null;
            for(String antecedent : r.getAntecedents()) {
                if(!root.containChild(String.valueOf(antecedent.charAt(0)))) {
                    classNode = new Node(String.valueOf(antecedent.charAt(0)));
                    root.getChilds().add(classNode);
                }
                for(Node cNode : root.getChilds()) {
                    if(cNode.getName().equals(antecedent.charAt(0))) {
                        if(!cNode.containChild(antecedent)) {
                            alphaNode = new Node(antecedent);
                            cNode.getChilds().add(alphaNode);
                        } else {
                            alphaNode = cNode.getChild(antecedent);
                        }
                        if(betaNode==null) {
                            betaNode = new Node(String.valueOf(r.getId()));
                            betaNode.setEquateRule(r);
                        }
                        betaNode.setNumberOfParents(betaNode.getNumberOfParents()+1);
                        betaNode.getChilds().add(alphaNode); //Child of a betaNode references to his parents
                        alphaNode.getChilds().add(betaNode);
                        betaNodes.add(betaNode);
                    }
                }
            }
        }
    }

    public void propagate (String fact) {
        Node cNode, alphaNode;
        if (root.containChild(String.valueOf(fact.charAt(0)))) {
            cNode = root.getChild(String.valueOf(fact.charAt(0)));
            if(cNode.containChild(fact)) {
                alphaNode = cNode.getChild(fact);
                for(Node child : alphaNode.getChilds()) {
                    if(!child.getMemory().contains(fact))
                        child.getMemory().add(fact);
                    if(child.getNumberOfParents() == child.getMemory().size()) {
                        successfulRules.add(child.getEquateRule());
                    }
                }
            }
        }
    }

    public List<Rule> getSuccessfulRules() {
        return this.successfulRules;
    }

    public List<Node> getBetaNodes() {
        return betaNodes;
    }

}

