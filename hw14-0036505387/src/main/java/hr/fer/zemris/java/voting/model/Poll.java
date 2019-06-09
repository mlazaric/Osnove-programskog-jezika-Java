package hr.fer.zemris.java.voting.model;

import hr.fer.zemris.java.voting.dao.DAOProvider;

import java.util.List;

public class Poll {

    private int id;
    private String title;
    private String message;
    private List<PollOption> options;

    public Poll(String title, String message, List<PollOption> options) {
        this(-1, title, message, options);
    }

    public Poll(int id, String title, String message, List<PollOption> options) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }

    public void save() {
        DAOProvider.getDao().savePoll(this);

        if (options != null) {
            for (PollOption option : options) {
                option.setPollId(id);
                option.save();
            }
        }
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", options=" + options +
                '}';
    }

}
