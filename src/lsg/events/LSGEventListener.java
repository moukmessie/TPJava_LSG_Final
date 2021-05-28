package lsg.events;

public interface LSGEventListener<T extends LSGEvent> {

    public void handle(T event) ;

}
