package events;

public abstract class EventHandler<T extends Event> {

    public abstract void handle(T ev);

}
