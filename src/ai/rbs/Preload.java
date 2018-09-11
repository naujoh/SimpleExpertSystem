package ai.rbs;

import ai.rbs.knowledge.KnowledgeBase;
import ai.rbs.knowledge.Rule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Preload {
    private String fileName;
    public Preload(String fileName) {
        this.fileName = fileName;
    }

    public void createBinaryFile() {
        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line, id, consequent="";
            StringTokenizer stringTokenizer;
            List<String> antedecents = new ArrayList<>();
            KnowledgeBase knowledgeBase = new KnowledgeBase();

            while((line = bufferedReader.readLine()) != null) {
                stringTokenizer = new StringTokenizer(line);
                id = stringTokenizer.nextToken();
                consequent = stringTokenizer.nextToken();
                while(stringTokenizer.hasMoreElements()) {
                    antedecents.add(stringTokenizer.nextToken());
                }

                knowledgeBase.insertRule(new Rule(
                        Integer.parseInt(id),
                        consequent,
                        antedecents
                ));
                antedecents.clear();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
