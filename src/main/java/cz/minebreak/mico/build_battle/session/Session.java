package cz.minebreak.mico.build_battle.session;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class Session {

    protected List<Player> playingPlayers;

    public void setPlayingPlayers(List<Player> playingPlayers) {
        this.playingPlayers = playingPlayers;
    }

    public abstract boolean start(Object... args);
    public abstract void stop();
    public abstract void reset();
}