package hr.fer.zemris.java.voting.model;

import hr.fer.zemris.java.voting.dao.DAOProvider;

import java.util.List;

/**
 * Models a single poll which has multiple options which the user can vote for.
 *
 * @author Marko Lazarić
 */
public class Poll {

    /**
     * The unique identifier of the poll.
     */
    private int id;

    /**
     * The title of the poll.
     */
    private String title;

    /**
     * The description of the poll.
     */
    private String message;

    /**
     * The poll options associated with this poll.
     */
    private List<PollOption> options;

    /**
     * Creates a new {@link Poll} with the given arguments.
     *
     * @param title the title of the poll
     * @param message the description of the poll
     * @param options the poll options associated with this poll
     */
    public Poll(String title, String message, List<PollOption> options) {
        this(-1, title, message, options);
    }

    /**
     * Creates a new {@link Poll} with the given arguments.
     *
     * @param id the unique identifier of the poll
     * @param title the title of the poll
     * @param message the description of the poll
     * @param options the poll options associated with this poll
     */
    public Poll(int id, String title, String message, List<PollOption> options) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.options = options;
    }

    /**
     * Returns the unique identifier of the poll.
     *
     * @return the unique identifier of the poll
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the poll to the new value.
     *
     * @param id the new unique identifier of the poll
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the title of the poll.
     *
     * @return the title of the poll
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the poll to the new value.
     *
     * @param title the new title of the poll
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of the poll.
     *
     * @return the description of the poll
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the description of the poll to thew new value.
     *
     * @param message the new description of the poll
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the options associated with this poll.
     *
     * @return the options associated with this poll
     */
    public List<PollOption> getOptions() {
        return options;
    }

    /**
     * Sets the options associated with this poll to the new value.
     *
     * @param options the new options associated with this poll
     */
    public void setOptions(List<PollOption> options) {
        this.options = options;
    }

    /**
     * Saves the poll using {@link DAOProvider}. If its ID is -1, it will create a new poll, otherwise
     * it will update the existing poll with the same ID.
     *
     * It will also call {@code save()} for all  {@link PollOption}s associated with this poll.
     */
    public void save() {
        if (id == -1) {
            DAOProvider.getDao().createPoll(this);
        }
        else {
            DAOProvider.getDao().updatePoll(this);
        }

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
