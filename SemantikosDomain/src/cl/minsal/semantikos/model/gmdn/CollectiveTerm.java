package cl.minsal.semantikos.model.gmdn;

import java.util.List;

/**
 * Created by des01c7 on 20-11-17.
 */
public class CollectiveTerm {

    private long code;

    private String termName;

    private String termDefinition;

    private List<CollectiveTerm> children;

    public CollectiveTerm(long code, String termName, String termDefinition) {
        this.code = code;
        this.termName = termName;
        this.termDefinition = termDefinition;
    }

    public CollectiveTerm(long code, String termName, String termDefinition, List<CollectiveTerm> children) {
        this.code = code;
        this.termName = termName;
        this.termDefinition = termDefinition;
        this.children = children;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermDefinition() {
        return termDefinition;
    }

    public void setTermDefinition(String termDefinition) {
        this.termDefinition = termDefinition;
    }

    public List<CollectiveTerm> getChildren() {
        return children;
    }

    public void setChildren(List<CollectiveTerm> children) {
        this.children = children;
    }
}
