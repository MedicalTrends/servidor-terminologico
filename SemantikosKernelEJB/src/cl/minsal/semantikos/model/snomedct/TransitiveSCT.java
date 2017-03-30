package cl.minsal.semantikos.model.snomedct;

/**
 * Created by des01c7 on 20-03-17.
 */
public class TransitiveSCT {

    private long idPartent;
    private long idChild;

    public TransitiveSCT(long idPartent, long idChild) {
        this.idPartent = idPartent;
        this.idChild = idChild;
    }

    public long getIdPartent() {
        return idPartent;
    }

    public void setIdPartent(long idPartent) {
        this.idPartent = idPartent;
    }

    public long getIdChild() {
        return idChild;
    }

    public void setIdChild(long idChild) {
        this.idChild = idChild;
    }
}
