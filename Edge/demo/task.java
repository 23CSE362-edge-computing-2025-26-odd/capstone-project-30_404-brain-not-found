package org.fog.test.perfeval;

public class task implements Comparable<task> {
    public enum Type {CRITICAL, DIETARY, BATTERY}
    public Type type;
    public int value;
    public int priority;

    public task(Type type, int value, int priority) {
        this.type = type;
        this.value = value;
        this.priority = priority;
    }

    @Override
    public int compareTo(task other) {
        return Integer.compare(this.priority, other.priority);
    }
}
