package utils;

import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.LinkedList;
import java.util.Queue;

public class CommandScheduler {
    private final Queue<Runnable> commandQueue = new LinkedList<>();
    private final ElapsedTime timer = new ElapsedTime();
    private double delayEndTime = 0;
    private boolean isRunning = false;

    public void scheduleCommand(Runnable command) {
        commandQueue.add(command);
    }

    public void scheduleDelay(double seconds) {
        commandQueue.add(() -> delayEndTime = timer.seconds() + seconds);
    }

    public void update() {
        if (!isRunning || commandQueue.isEmpty()) return;

        Runnable currentCommand = commandQueue.peek();
        if (currentCommand == null) return;

        // Если это задержка
        if (delayEndTime > 0) {
            if (timer.seconds() >= delayEndTime) {
                delayEndTime = 0; // Сброс задержки
                commandQueue.remove(); // Удаляем задержку из очереди
            }
        }
        // Если это обычная команда
        else {
            currentCommand.run();
            commandQueue.remove();
        }
    }

    public void start() {
        timer.reset();
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }
}