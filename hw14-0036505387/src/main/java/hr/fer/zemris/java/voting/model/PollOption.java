package hr.fer.zemris.java.voting.model;

import hr.fer.zemris.java.voting.dao.DAOProvider;

/**
 * Models a single poll option which the user can vote for.
 *
 * @author Marko Lazarić
 */
public class PollOption {

    /**
     * The unique identifier of the option.
     */
    private int id;

    /**
     * The title of the poll option.
     */
    private String title;

    /**
     * The link for the poll option.
     */
    private String link;

    /**
     * The unique identifier of the poll whose option this is.
     */
    private int pollId;

    /**
     * The number of votes this option has received.
     */
    private int voteCount;

    /**
     * Creates a new {@link PollOption} with the given arguments.
     *
     * @param title the title of the poll option
     * @param link the link for the poll option
     */
    public PollOption(String title, String link) {
        this(-1, title, link, -1, 0);
    }

    /**
     * Creates a new {@link PollOption} with the given arguments.
     *
     * @param title the title of the poll option
     * @param link the link for the poll option
     * @param voteCount the number of votes this option has received
     */
    public PollOption(String title, String link, int voteCount) {
        this(-1, title, link, -1, voteCount);
    }

    /**
     * Creates a new {@link PollOption} with the given arguments.
     *
     * @param id the unique identifier of the option
     * @param title the title of the poll option
     * @param link the link for the poll option
     * @param pollId the unique identifier of the poll whose option this is
     * @param voteCount the number of votes this option has received
     */
    public PollOption(int id, String title, String link, int pollId, int voteCount) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.pollId = pollId;
        this.voteCount = voteCount;
    }

    /**
     * Returns the unique identifier of the option.
     *
     * @return the unique identifier of the option
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the the unique identifier of the option to the new value.
     *
     * @param id the new unique identifier of the option
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the title of the poll option.
     *
     * @return the title of the poll option
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the poll option to the new value.
     *
     * @param title the new title of the poll option
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the link for the poll option.
     *
     * @return the link for the poll option
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link for the poll option to the new value.
     *
     * @param link the new link for the poll option
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Returns the unique identifier of the poll whose option this is.
     *
     * @return the unique identifier of the poll whose option this is
     */
    public int getPollId() {
        return pollId;
    }

    /**
     * Sets the the unique identifier of the poll whose option this is to the new value.
     *
     * @param pollId the new unique identifier of the poll whose option this is
     */
    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    /**
     * Returns the number of votes this option has received.
     *
     * @return the number of votes this option has received
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     * Sets the number of votes this option has received to the new value.
     *
     * @param voteCount the new number of votes this option has received
     */
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    /**
     * Saves the poll option using {@link DAOProvider}. If its ID is -1, it will create a new poll option, otherwise
     * it will update the existing poll option with the same ID.
     */
    public void save() {
        if (id == -1) {
            DAOProvider.getDao().createPollOption(this);
        }
        else {
            DAOProvider.getDao().updatePollOption(this);
        }
    }

    @Override
    public String toString() {
        return "PollOption{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", pollId=" + pollId +
                ", voteCount=" + voteCount +
                '}';
    }

}
