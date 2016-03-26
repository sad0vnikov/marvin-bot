package events;

import java.util.HashSet;
import java.util.Set;

/**
 * Базовый класс для объектов, подписывающихся на сообщения определённого типа
 * и отправляющий их EventHandler'ам
 *
 * @param <T>
 */
public abstract class EventListener<T extends Event>{

    public abstract void send(T ev);

}
