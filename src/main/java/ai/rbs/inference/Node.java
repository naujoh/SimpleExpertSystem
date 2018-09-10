package main.java.ai.rbs.inference;

import main.java.ai.rbs.knowledge.Rule;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;
    private List<Node> childs;
    private int numberOfParents;
    private List<String> memory;
    private Rule equateRule;

    public Node (String name) {
        this.name = name;
        this.numberOfParents = 0;
        this.childs = new ArrayList<>();
        this.memory = new ArrayList<>();
    }


    public boolean containChild(String test) {
        for(Node n : childs) {
            if(n.name.equals(test))
                return true;
        }
        return false;
    }

    public Node getChild (String nodeName) {
        for(Node n : childs) {
            if(n.name.equals(nodeName)) {
                return n;
            }
        }
        return null;
    }

    public Rule getEquateRule() {
        return equateRule;
    }

    public void setEquateRule(Rule r) {
        this.equateRule = r;
    }

    public String getName() {
        return name;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public List<String> getMemory() {
        return memory;
    }

    public void setNumberOfParents(int i) {
        numberOfParents = i;
    }

    public int getNumberOfParents() {
        return numberOfParents;
    }
}
