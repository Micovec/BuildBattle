package cz.minebreak.mico.build_battle.vote;

public class VoteOption {

    private String name;
    private int voteNumber;
    private int votes;

    public VoteOption(String name, int voteNumber) {
        this.name = name;
        this.voteNumber = voteNumber;
        resetVotes();
    }

    public void resetVotes() {
        votes = 0;
    }

    public void vote() {
        votes++;
    }

    public void unVote() {
        votes--;
    }

    public String getName() {
        return name;
    }

    public int getVoteNumber() {
        return voteNumber;
    }

    public int getVotes() {
        return votes;
    }
}
