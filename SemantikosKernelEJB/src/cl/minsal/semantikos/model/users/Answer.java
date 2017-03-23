package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.PersistentEntity;

/**
 * @author Diego Soto
 */
public class Answer extends PersistentEntity {

    public static final Answer DUMMY_QUESTION = new Answer();

    private long id;

    private String answer;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
