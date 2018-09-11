package ai.rbs;

import ai.rbs.inference.InferenceEngine;
import ai.rbs.inference.RETENetwork;
import ai.rbs.knowledge.KnowledgeBase;

public class Main {

    public static void main(String[] args) {
        Preload p = new Preload("RULES.txt");
        p.createBinaryFile();

        UI ui = new UI();
        ui.createUI();
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        /*InferenceEngine inferenceEngine = new InferenceEngine();
        RETENetwork reteNetwork = new RETENetwork();
        reteNetwork.create(knowledgeBase.getRules());
        System.out.println("reteNework created");*/
        knowledgeBase.setGoalsFacts();
        for(String goal : knowledgeBase.getTargetFact()) {
            System.out.println(goal);
        }

    }
}
