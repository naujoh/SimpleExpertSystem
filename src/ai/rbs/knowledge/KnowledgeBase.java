package ai.rbs.knowledge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KnowledgeBase {
    private FilesManager filesManager;
    private List<String> targetFact;

    public Rule getRandomRule() {
        Rule r = new Rule();
        Random random = new Random();
        int nRegisters, logicAddress, randomId, filePointer;
        String antecedent;
        try {
            filesManager = new FilesManager("index");
            nRegisters = (int) filesManager.getFile().length() / FilesManager.INDEX_SIZE_REGISTER;
            filesManager.getFile().seek((random.nextInt(nRegisters))*FilesManager.INDEX_SIZE_REGISTER);
            randomId = filesManager.getFile().readInt();
            while (randomId < 1) {
                filesManager.getFile().seek((random.nextInt(nRegisters) + 1)*FilesManager.INDEX_SIZE_REGISTER);
                filesManager.getFile().seek(filesManager.getFile().getFilePointer()-FilesManager.INDEX_SIZE_REGISTER);
                randomId = filesManager.getFile().readInt();
            }
            logicAddress = filesManager.getFile().readInt();
            filesManager.getFile().close();
            filesManager = new FilesManager("master");
            filesManager.getFile().seek(logicAddress);
            r.setId(filesManager.getFile().readInt());
            r.setConsequent(filesManager.readString(5).trim());
            filePointer = (int) filesManager.getFile().getFilePointer();
            while (filesManager.getFile().getFilePointer() < 10 * (4 * Character.BYTES) + filePointer) {
                antecedent = filesManager.readString(4).trim();
                if(!antecedent.equals("null"))
                    r.addAntecedent(antecedent);
            }
            filesManager.getFile().close();
        } catch (Exception e) { e.printStackTrace(); }
        return r;
    }

    public List<Rule> getRules() {
        List<Rule> rules = new ArrayList<>();
        Rule r;
        int ruleId, filePointer;
        String antecedent;
        try {
            filesManager = new FilesManager("master");
            filesManager.getFile().seek(0);
            while (filesManager.getFile().getFilePointer() < filesManager.getFile().length()) {
                ruleId = filesManager.getFile().readInt();
                if(ruleId > 0) {
                    r = new Rule();
                    r.setId(ruleId);
                    r.setConsequent(filesManager.readString(5).trim());
                    filePointer = (int) filesManager.getFile().getFilePointer();
                    while (filesManager.getFile().getFilePointer() < 10 * (4 * Character.BYTES) + filePointer) {
                        antecedent = filesManager.readString(4).trim();
                        if(!antecedent.equals("null"))
                            r.addAntecedent(antecedent);
                    }
                    rules.add(r);
                } else { filesManager.getFile().seek(filesManager.getFile().getFilePointer()+(FilesManager.MASTER_SIZE_REGISTER - Integer.BYTES)); }
            }
            filesManager.getFile().close();
        } catch (Exception e) { e.printStackTrace(); }
        return rules;
    }

    public List<Rule> getDeletedRules() {
        List<Rule> rules = new ArrayList<>();
        int ruleId, filePointer;
        String antecedent;
        Rule r;
        try {
            filesManager = new FilesManager("master");
            filesManager.getFile().seek(0);
            while (filesManager.getFile().getFilePointer() < filesManager.getFile().length()) {
                ruleId = filesManager.getFile().readInt();
                if(ruleId < 0) {
                    r = new Rule();
                    r.setId(ruleId);
                    r.setConsequent(filesManager.readString(5).trim());
                    filePointer = (int) filesManager.getFile().getFilePointer();
                    while (filesManager.getFile().getFilePointer() < 10 * (4 * Character.BYTES) + filePointer) {
                        antecedent = filesManager.readString(4).trim();
                        if(!antecedent.equals("null"))
                            r.addAntecedent(antecedent);
                    }
                    rules.add(r);
                } else { filesManager.getFile().seek(filesManager.getFile().getFilePointer()+(FilesManager.MASTER_SIZE_REGISTER - Integer.BYTES)); }
            }
            filesManager.getFile().close();
        } catch (Exception e) { e.printStackTrace(); }
        return rules;
    }

    public boolean insertRule(Rule r) {
        boolean inserted = false;
        int logicAddress;
        StringBuffer buffer;
        try {
            filesManager = new FilesManager("index");
            r.setId((int) (filesManager.getFile().length() / FilesManager.INDEX_SIZE_REGISTER) + 1);
            filesManager.getFile().close();
            filesManager = new FilesManager("master");
            logicAddress = (int) filesManager.getFile().length();
            filesManager.getFile().seek(logicAddress);
            filesManager.getFile().writeInt(r.getId());
            buffer = new StringBuffer(r.getConsequent());
            buffer.setLength(5);
            filesManager.getFile().writeChars(buffer.toString());

            if(r.getAntecedents().size() >= 10) {
                for (int i = 0; i < 10; i++) {
                    buffer = new StringBuffer(r.getAntecedents().get(i));
                    buffer.setLength(4);
                    filesManager.getFile().writeChars(buffer.toString());
                }
            }else {
                for (String s : r.getAntecedents()) {
                    buffer = new StringBuffer(s);
                    buffer.setLength(4);
                    filesManager.getFile().writeChars(buffer.toString());
                }
                for(int i=0; i < (10 - r.getAntecedents().size()); i++) {
                    filesManager.getFile().writeChars("null");
                }
            }

            filesManager.getFile().close();
            filesManager = new FilesManager("index");
            filesManager.getFile().seek(filesManager.getFile().length());
            filesManager.getFile().writeInt(r.getId());
            filesManager.getFile().writeInt(logicAddress);
            filesManager.getFile().close();
            inserted = true;
        } catch (Exception e) { e.printStackTrace(); }
        return inserted;
    }

    public boolean modifyRuleStatus(int id) {
        boolean deleted = false;
        int logicAddress = -1;
        try {
            filesManager = new FilesManager("index");
            filesManager.getFile().seek(0);
            while (filesManager.getFile().getFilePointer() < filesManager.getFile().length()) {
                if(filesManager.getFile().readInt() == id) {
                    logicAddress = filesManager.getFile().readInt();
                    filesManager.getFile().seek(filesManager.getFile().getFilePointer() - FilesManager.INDEX_SIZE_REGISTER);
                    filesManager.getFile().writeInt(-1*(id));
                    break;
                } else { filesManager.getFile().readInt(); }
            }
            filesManager.getFile().close();
            if(logicAddress != -1) {
                filesManager = new FilesManager("master");
                filesManager.getFile().seek(logicAddress);
                filesManager.getFile().writeInt(-1*(id));
                filesManager.getFile().close();
                deleted = true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return deleted;
    }

    public boolean modifyRule(Rule r) {
        boolean modified = false;
        int logicAddress = -1;
        StringBuffer buffer;
        try {
            filesManager = new FilesManager("index");
            filesManager.getFile().seek(0);
            while (filesManager.getFile().getFilePointer() < filesManager.getFile().length()) {
                if(filesManager.getFile().readInt() == r.getId()) {
                    logicAddress = filesManager.getFile().readInt();
                    break;
                } else { filesManager.getFile().readInt(); }
            }
            filesManager.getFile().close();
            if(logicAddress != -1) {
                filesManager = new FilesManager("master");
                filesManager.getFile().seek(logicAddress);
                filesManager.getFile().writeInt(r.getId());
                buffer = new StringBuffer(r.getConsequent());
                buffer.setLength(5);
                filesManager.getFile().writeChars(buffer.toString());

                if(r.getAntecedents().size() >= 10) {
                    for (int i = 0; i < 10; i++) {
                        buffer = new StringBuffer(r.getAntecedents().get(i));
                        buffer.setLength(4);
                        filesManager.getFile().writeChars(buffer.toString());
                    }
                } else {
                    for (String s : r.getAntecedents()) {
                        buffer = new StringBuffer(s);
                        buffer.setLength(4);
                        filesManager.getFile().writeChars(buffer.toString());
                    }
                    for(int i=0; i < (10 - r.getAntecedents().size()); i++) {
                        filesManager.getFile().writeChars("null");
                    }
                }

                filesManager.getFile().close();
                modified = true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return modified;
    }

    public void setGoalsFacts() {
        targetFact = new ArrayList<>();
        String consequent;
        boolean isGoal;
        List<Rule> rules = getRules();
        for(Rule r : rules) {
            isGoal = true;
            consequent = r.getConsequent();
            for(Rule d : rules) {
                if(d.getAntecedents().contains(consequent)) {
                    isGoal = false;
                    break;
                }
            }
            if(isGoal)
                if(!targetFact.contains(consequent))
                    targetFact.add(consequent);
        }
    }

    public List<String> getTargetFact() {
        return targetFact;
    }
}
