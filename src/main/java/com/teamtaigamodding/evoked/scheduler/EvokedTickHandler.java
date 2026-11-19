package com.teamtaigamodding.evoked.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Runs delayed tasks on the server thread. (The 1.19.2 version used a real-time executor
 * thread, which touched inventories off the main thread.)
 */
public class EvokedTickHandler {
    private static final List<Scheduled> TASKS = new ArrayList<>();
    private static long tick = 0;

    /** Schedules a task to run on the server thread after the given number of ticks. */
    public static void scheduleTask(Runnable run, int delayTicks) {
        TASKS.add(new Scheduled(tick + Math.max(1, delayTicks), run));
    }

    public static void onServerTick(ServerTickEvent.Post event) {
        tick++;
        if (TASKS.isEmpty()) {
            return;
        }
        // Copy so tasks may schedule further tasks safely.
        List<Scheduled> due = new ArrayList<>();
        Iterator<Scheduled> it = TASKS.iterator();
        while (it.hasNext()) {
            Scheduled s = it.next();
            if (s.runAt <= tick) {
                due.add(s);
                it.remove();
            }
        }
        for (Scheduled s : due) {
            s.task.run();
        }
    }

    public static void onServerStopped(ServerStoppedEvent event) {
        TASKS.clear();
        tick = 0;
    }

    private record Scheduled(long runAt, Runnable task) {}
}
