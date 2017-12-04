package se.kth.g2.planner;

public class EventHandlerException extends RuntimeException{
    public String message;
    public Exception e;

    public EventHandlerException(String message, Exception e){
        this.message = message;
        this.e = e;
    }

    public EventHandlerException(String message){
        this.message = message;
    }

}
