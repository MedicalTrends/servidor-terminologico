package cl.minsal.semantikos.description;

/**
 * Created by des01c7 on 23-03-18.
 */
public class Suggestion {

    private long id;

    private String source;

    private String term;

    public Suggestion(long id, String source, String term) {
        this.id = id;
        this.source = source;
        this.term = term;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
