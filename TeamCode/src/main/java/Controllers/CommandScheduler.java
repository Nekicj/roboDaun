package Controllers;

import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.LinkedList;
import java.util.Queue;

public class CommandScheduler {
    private final Queue<Runnable> commandQueue = new LinkedList<>();
    private final ElapsedTime timer = new ElapsedTime();
    private double delayEndTime = 0;
    private boolean isRunning = false;
    private boolean isDelayActive = false;

    private boolean autoReset = true;

    public void scheduleCommand(Runnable command) {
        commandQueue.add(command);
    }

    public void scheduleDelay(double seconds) {
        commandQueue.add(() -> {
            delayEndTime = timer.seconds() + seconds;
            isDelayActive = true;
        });
    }

    public void update() {
        if (!isRunning || commandQueue.isEmpty()) return;

        // Обработка активной задержки
        if (isDelayActive) {
            if (timer.seconds() >= delayEndTime) {
                isDelayActive = false;
                commandQueue.remove();
            } else {
                return;
            }
        }

        Runnable currentCommand = commandQueue.peek();
        if (currentCommand == null) {
            commandQueue.remove();
            return;
        }

        currentCommand.run();
        commandQueue.remove();

        if (isDelayActive) {
            return;
        }
    }

    public void start() {
        if (autoReset) {
            clearQueue();
        }
        timer.reset();
        isRunning = true;
        isDelayActive = false;
    }

    public void stop() {
        isRunning = false;
    }

    public void clearQueue() {
        commandQueue.clear();
        delayEndTime = 0;
        isDelayActive = false;
    }

    public void setAutoReset(boolean autoReset) {
        this.autoReset = autoReset;
    }

    public boolean isRunning() {
        return isRunning && !commandQueue.isEmpty();
    }

    public boolean isDelayActive() {
        return isDelayActive;
    }
}