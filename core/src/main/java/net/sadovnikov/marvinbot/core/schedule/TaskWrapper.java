package net.sadovnikov.marvinbot.core.schedule;


import java.util.Date;
import java.util.Timer;

public class TaskWrapper {

    protected Task task;
    protected Date date;
    protected boolean isPeriodic;
    protected long period = 0;

    public TaskWrapper(Task task, Date scheduledDate) {
        this.task = task;
        this.date = scheduledDate;
    }

    public TaskWrapper(Task task, long runAfterMilliseconds) {
        Date now = new Date();
        long scheduleTimestamp = now.getTime() + runAfterMilliseconds;
        Date dateToRun = new Date(scheduleTimestamp);

        this.task = task;
        this.date = dateToRun;
    }

    public TaskWrapper(Task task, Date scheduledDate, long period) {
        this(task, scheduledDate);
        this.isPeriodic = true;
        this.period = period;
    }

    public TaskWrapper(Task task, long runAfterMilliseconds, long period) {
        this(task, runAfterMilliseconds);
        this.isPeriodic = true;
        this.period = period;
    }


    public Task task() {
        return task;
    }

    public Date date() {
        return date;
    }

    public boolean isPeriodic() {
        return isPeriodic;
    }

    public long period() {
        return period;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
