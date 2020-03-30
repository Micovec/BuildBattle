package cz.minebreak.mico.build_battle.vote;

import cz.minebreak.mico.build_battle.util.ThemeNotFoundException;
import org.bukkit.entity.Player;

import java.util.*;

public class Voting {

    private VoteOption[] voteOptions;
    private List<VoteListener> voteListeners;
    private Map<Player, VoteOption> playersVoted;

    public Voting(VoteOption[] voteOptions) {
        voteListeners = new ArrayList<>();
        this.voteOptions = voteOptions;
        playersVoted = new HashMap<>();
    }

    /**
     * Adds listener to listener collection
     * @param listener listener
     */
    public void addListener(VoteListener listener) {
        voteListeners.add(listener);
    }

    /**
     * Returns Vote at index 'index'
     * @param index vote's index
     * @return Vote at index. Null if index is higher or equal to option count.
     */
    public VoteOption getVoteAt(int index) {
        return voteOptions[index];
    }

    public int getVotesAt(int index) {
        return voteOptions[index].getVotes();
    }

    public void voteForOption(Player player, int optionNumber) throws ThemeNotFoundException {
        if (playersVoted.containsKey(player)) {
            VoteOption playersLastVotedOpt = playersVoted.get(player);
            playersLastVotedOpt.unVote();

            for (VoteListener voteListener : voteListeners) {
                voteListener.onUnVote(playersLastVotedOpt);
            }
        }

        VoteOption votedOption;
        try {
            votedOption = voteOptions[optionNumber];
            votedOption.vote();
            playersVoted.put(player, votedOption);
        } catch (IndexOutOfBoundsException e) {
            throw new ThemeNotFoundException("Theme number: " + optionNumber);
        }

        for (VoteListener voteListener : voteListeners) {
            voteListener.onVote(votedOption);
        }
    }

    public VoteOption getWinningVote() {
        VoteOption winningVoteOption = null;
        for (VoteOption voteOption : voteOptions) {
            if (winningVoteOption == null || voteOption.getVotes() > winningVoteOption.getVotes()) {
                winningVoteOption = voteOption;
            }
        }

        return winningVoteOption;
    }

    public void resetVoting() {
        for (VoteOption voteOption : voteOptions) {
            voteOption.resetVotes();
        }

        playersVoted.clear();
    }
}
