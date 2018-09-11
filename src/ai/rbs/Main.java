package ai.rbs;

public class Main {

    public static void main(String[] args) {
<<<<<<< HEAD
        /*Preload p = new Preload("RULES.txt");
        p.createBinaryFile();*/
        /*KnowledgeBase b = new KnowledgeBase();
        for(Rule r : b.getRules()) {
            System.out.println(r.getId()+"");
        }*/
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        InferenceEngine inferenceEngine = new InferenceEngine();
        RETENetwork reteNetwork = new RETENetwork();
        reteNetwork.create(knowledgeBase.getRules());
        knowledgeBase.setGoalsFacts();
        FactBase factBase = new FactBase();
        factBase.getFacts().add("e1");
        factBase.getFacts().add("e2");
        factBase.getFacts().add("e3");
        factBase.getFacts().add("e4");
        factBase.getFacts().add("e5");
        factBase.getFacts().add("e6");
        factBase.getFacts().add("e11");
        factBase.getFacts().add("e12");
        factBase.getFacts().add("e14");
        factBase.getFacts().add("e15");
        factBase.getFacts().add("e8");
        factBase.getFacts().add("e16");

        //factBase.getFacts().add("e13");
        JustificationModule justificationModule = new JustificationModule();
        if(inferenceEngine.forwardChaining(factBase.getFacts(), knowledgeBase, reteNetwork, justificationModule)) {
            System.out.println("El sujeto esta consumiendo " + factBase.getFacts().get(factBase.getFacts().size()-1));
        } else {
            System.out.println("No hay suficientes datos para completar el encadenamiento");
        }
        String e;
        for(String[] s : justificationModule.getJustification()) {
            e = "";
            for(String el : s) {
                e += el + "\t";
            }
            System.out.println(e);
        }
=======
        Preload p = new Preload("RULES.txt");
        p.createBinaryFile();
        UI ui = new UI();
        ui.createUI();
>>>>>>> eae218d65d85465c6fee11842623463398b068b1

        /*for(String s : factBase.getFacts()) {
            System.out.printf(s + " \t");
        }*/
    }
}
