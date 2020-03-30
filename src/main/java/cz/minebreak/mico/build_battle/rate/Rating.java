package cz.minebreak.mico.build_battle.rate;

import cz.minebreak.mico.build_battle.util.InvalidRateException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Rating {

    private RateOption[] rateOptions;
    private Map<Player, RateOption> playerRates;

    public Rating(RateOption[] rateOptions) {
        this.rateOptions = rateOptions;
        playerRates = new HashMap<>();
    }

    public void rate(Player player, int rateOption) throws InvalidRateException{
        if (playerRates.containsKey(player)) {
            playerRates.get(player).unRate();
        }

        try {
            rateOptions[rateOption].rate();
            playerRates.put(player, rateOptions[rateOption]);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidRateException(String.valueOf(rateOption));
        }
    }

    public RateOption[] getRateOptions() {
        return rateOptions;
    }

    public int getTotalRate() {
        int totalRate = 0;

        for (RateOption rateOption : rateOptions) {
            totalRate += rateOption.getCount() * rateOption.getWeight();
        }

        return totalRate;
    }
}
