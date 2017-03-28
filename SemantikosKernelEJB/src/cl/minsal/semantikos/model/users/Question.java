package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.PersistentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Diego Soto
 */
public class Question extends PersistentEntity {

    public static final Question DUMMY_QUESTION = new Question();

    private long id;

    private String question;

    private boolean selected;

    private List<Answer> answers;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return id == question.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
