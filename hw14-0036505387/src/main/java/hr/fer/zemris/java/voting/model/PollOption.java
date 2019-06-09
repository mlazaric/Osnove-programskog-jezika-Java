package hr.fer.zemris.java.voting.model;

import hr.fer.zemris.java.voting.dao.DAOProvider;

public class PollOption {

    private int id;
    private String title;
    private String link;
    private int pollId;
    private int voteCount;

    public PollOption(String title, String link) {
        this(-1, title, link, -1, 0);
    }

    public PollOption(int id, String title, String link, int pollId, int voteCount) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.pollId = pollId;
        this.voteCount = voteCount;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void save() {
        DAOProvider.getDao().savePollOption(this);
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
