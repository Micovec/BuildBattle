package cz.minebreak.mico.build_battle.rate;

public class RateOption {

    private String name;
    private int weight;
    private int count;

    public RateOption(String name, int weight) {
        this.name = name;
        this.weight = weight;
        count = 0;
    }

    public void rate() {
        count++;
    }

    public void unRate() {
        count--;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getWeight() {
        return weight;
    }
}
