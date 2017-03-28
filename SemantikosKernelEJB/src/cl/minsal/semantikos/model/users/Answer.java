package cl.minsal.semantikos.model.users;

import cl.minsal.semantikos.model.PersistentEntity;

/**
 * @author Diego Soto
 */
public class Answer extends PersistentEntity {


    private long id;

    private String answer;

    private User user;

    private Question question;

    @Override
    public long getId() {
        return id;
    }

    public Answer() {
    }

    public Answer(Question question) {
        this.question = question;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer1 = (Answer) o;

        if (answer != null ? !answer.equals(answer1.answer) : answer1.answer != null) return false;
        return question != null ? question.equals(answer1.question) : answer1.question == null;

    }
    */

    @Override
    public boolean equals(Object other) {

        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Answer that = (Answer) other;

        /* 1. Se compara el usuario  */
        if (this.getUser() != that.getUser()) return false;

        /* 2. Se compara la pregunta */
        if(!this.getQuestion().equals(that.getQuestion())) return false;

        /* 3. Si compara la respuesta */
        if(!this.getAnswer().equals(that.getAnswer())) return false;

        return true;

    }

    @Override
    public int hashCode() {
        int result = answer != null ? answer.hashCode() : 0;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        return result;
    }
}
