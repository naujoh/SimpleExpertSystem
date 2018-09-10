package ai.rbs;

import java.util.ArrayList;
import java.util.List;

public class FactBase {
    private List<String> facts;

    public FactBase() {
        facts = new ArrayList<String>();
    }

    public List<String> getFacts() {
        return facts;
    }
}
