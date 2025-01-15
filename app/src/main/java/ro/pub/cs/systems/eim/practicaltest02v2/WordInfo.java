package ro.pub.cs.systems.eim.practicaltest02v2;

public class WordInfo {
    private final String definition;


    public WordInfo(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return "WordInfo{" +
                "definition='" + definition + '\'' +
                '}';
    }
}
