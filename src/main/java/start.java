
/*
import java.util.ArrayList;
import java.util.Observable;

public class model.Model extends Observable {
    private ArrayList<String> strings;

    public model.Model() {
        super();
        strings = new ArrayList<String>();
    }

    public void addString(String s) {
        strings.add(s);

        // Mark as changed and inform the observers
        super.setChanged();
        super.notifyObservers();
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void delete() {
        strings.clear();

        // Mark as changed and inform the observers
        super.setChanged();
        super.notifyObservers();
    }

} */