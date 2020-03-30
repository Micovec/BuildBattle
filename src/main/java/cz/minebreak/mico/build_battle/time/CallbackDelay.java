package cz.minebreak.mico.build_battle.time;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CallbackDelay {

    private ICallback iCallback;
    private long delayTicks;
    private JavaPlugin sender;
    private Object[] args;

    private BukkitRunnable runnable;

    public CallbackDelay(ICallback iCallback, long delayTicks, JavaPlugin sender, Object... args) {
        this.iCallback = iCallback;
        this.delayTicks = delayTicks;
        this.sender = sender;
        this.args = args;
    }

    public void run() {
        runnable = new BukkitRunnable(){
            @Override
            public void run() {
                iCallback.callbackDelay(sender, args);
            }
        };
        runnable.runTaskLater(sender, delayTicks);
    }

    public void cancel() {
        if (runnable != null)
            runnable.cancel();
    }

    /*
        Getters
     */
    public long getDelayTicks() {
        return delayTicks;
    }

    public Object[] getArgs() {
        return args;
    }

    public JavaPlugin getSender() {
        return sender;
    }

    public ICallback getICallback() {
        return iCallback;
    }

    /*
        Setters
     */
    public void setDelayTicks(long delayTicks) {
        this.delayTicks = delayTicks;
    }

    public void setArgs(Object... args) {
        this.args = args;
    }

    public void setSender(JavaPlugin sender) {
        this.sender = sender;
    }

    public void setICallback(ICallback iCallback) {
        this.iCallback = iCallback;
    }
}
