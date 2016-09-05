package net.sadovnikov.marvinbot.core.schedule;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

public class TasksSchedule {

    Set<TaskWrapper> tasks = new HashSet<>();
    Timer timer = new Timer();

    public TaskWrapper addTask(Task task, Date dateSchedule) {
        TaskWrapper wrapper = new TaskWrapper(task, dateSchedule);
        timer.schedule(task, dateSchedule);
        tasks.add(wrapper);

        return wrapper;
    }

    public TaskWrapper addTask(Task task, long runAfterMilliseconds) {
        TaskWrapper wrapper = new TaskWrapper(task, runAfterMilliseconds);
        timer.schedule(task, runAfterMilliseconds);
        tasks.add(wrapper);

        return wrapper;
    }

    public TaskWrapper addTask(Task task, Date dateSchedule, long period) {
        TaskWrapper wrapper = new TaskWrapper(task, dateSchedule, period);
        timer.schedule(task, dateSchedule, period);
        tasks.add(wrapper);

        return wrapper;
    }

    public TaskWrapper addTask(Task task, long runAfterMilliseconds, long period) {
        TaskWrapper wrapper = new TaskWrapper(task, runAfterMilliseconds, period);
        timer.schedule(task, runAfterMilliseconds, period);
        tasks.add(wrapper);

        return wrapper;
    }

    public void purge() {
        timer.purge();
    }
}
