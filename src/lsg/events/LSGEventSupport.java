package lsg.events;

import java.util.ArrayList;

public class LSGEventSupport<T extends LSGEvent> {

    ArrayList<LSGEventListener> listeners = new ArrayList<>() ;

    public synchronized void addListener(LSGEventListener listener){
        if(!listeners.contains(listener)){
            listeners.add(listener) ;
        }
    }

    public synchronized void removeListener(LSGEventListener listener){
        listeners.remove(listener) ;
    }

    public void fireEvent(T event){

        ArrayList<LSGEventListener> temporaryList ;

        synchronized (this){
            if(listeners.size() == 0) return;
            temporaryList = (ArrayList<LSGEventListener>)listeners.clone() ;
        }

        for(LSGEventListener listener: temporaryList){
            listener.handle(event);
        }

    }

}
