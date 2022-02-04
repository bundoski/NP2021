package auds.aud6Generics.PriorityQueue;

import java.util.ArrayList;
import java.util.List;

class PriorityQueueElement<T> implements Comparable<PriorityQueueElement<T>>{

    private T element;
    private int priority;

    public PriorityQueueElement(T element, int priority) {
        this.element = element;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PriorityQueueElement{" +
                "element=" + element +
                ", priority=" + priority +
                '}';
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(PriorityQueueElement<T> other) {
        return Integer.compare(this.priority, other.priority);
    }
}

public class PriorityQueue<T> {

    private List<PriorityQueueElement<T>> elements;

    public PriorityQueue(){
        elements = new ArrayList<>();
    }

    public void add(T item, int priority){
        PriorityQueueElement<T> newElement = new PriorityQueueElement<>(item, priority);

        int i;
        for(i = 0;i < elements.size();i++){
            if(newElement.compareTo(elements.get(i))<=0) break;
        }
        elements.add(i, newElement);
    }

    public T remove(){

        if(elements.size()==0){
            return null;
        }
        return elements.remove(elements.size()-1).getElement();
    }

}
