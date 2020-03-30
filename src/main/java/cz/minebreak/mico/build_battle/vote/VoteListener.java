package cz.minebreak.mico.build_battle.vote;

public interface VoteListener {
    void onVote(VoteOption voteOption);
    void onUnVote(VoteOption voteOption);
}
