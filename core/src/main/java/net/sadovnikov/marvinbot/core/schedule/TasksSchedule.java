package net.sadovnikov.marvinbot.core.schedule;

import org.apache.logging.log4j.LogManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

public class TasksSchedule {

    protected Set<TaskWrapper> tasks = new HashSet<>();
    protected Timer timer = new Timer();

    public TaskWrapper addTask(Task task, Date dateSchedule) {
        TaskWrapper wrapper = new TaskWrapper(task, dateSchedule);
        timer.schedule(task, dateSchedule);
        LogManager.getLogger("core-logger").debug("added scheduled task: " + task.getClass().getCanonicalName()
                + ", execution at " + dateSchedule.toString());
        tasks.add(wrapper);

        return wrapper;
    }

    public TaskWrapper addTask(Task task, long runAfterMilliseconds) {
        TaskWrapper wrapper = new TaskWrapper(task, runAfterMilliseconds);
        timer.schedule(task, runAfterMilliseconds);
        tasks.add(wrapper);

        LogManager.getLogger("core-logger").debug("added scheduled task: " + task.getClass().getCanonicalName()
                + ", execution in " + runAfterMilliseconds + " ms");

        return wrapper;
    }

    public TaskWrapper addTask(Task task, LocalDateTime dateTime) {
        return addTask(task, getMillisBeforeExecution(dateTime));
    }

    public TaskWrapper addTask(Task task, Date dateSchedule, long period) {
        TaskWrapper wrapper = new TaskWrapper(task, dateSchedule, period);
        timer.schedule(task, dateSchedule, period);
        tasks.add(wrapper);

        LogManager.getLogger("core-logger").debug("added scheduled task: " + task.getClass().getCanonicalName()
                + ", execution at " + dateSchedule.toString() + " period = " + period + " ms");

        return wrapper;
    }

    public TaskWrapper addTask(Task task, LocalDateTime dateSchedule, long period) {
        return addTask(task, getMillisBeforeExecution(dateSchedule), period);
    }

    public TaskWrapper addTask(Task task, long runAfterMilliseconds, long period) {
        TaskWrapper wrapper = new TaskWrapper(task, runAfterMilliseconds, period);
        timer.schedule(task, runAfterMilliseconds, period);
        tasks.add(wrapper);
        LogManager.getLogger("core-logger").debug("added scheduled task: " + task.getClass().getCanonicalName()
                + ", execution in " + runAfterMilliseconds + " ms, period = " + period + " ms");
        return wrapper;
    }

    protected long getMillisBeforeExecution(LocalDateTime dateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        long runAfter = currentTime.until(dateTime, ChronoUnit.MILLIS);
        return runAfter;
    }

    public void purge() {
        timer.purge();
    }
}
