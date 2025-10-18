package com.mycompany.example12.web;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class TodoItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private boolean done;
    private String label;

    public TodoItem() {
        // NOOP
    }

    public TodoItem(String label) {
        this(UUID.randomUUID(), false, label);
    }

    public TodoItem(UUID id, boolean done, String label) {
        this.id = id;
        this.done = done;
        this.label = label;
    }

    public UUID getId() {
        return id;
    }

    public boolean isDone() {
        return done;
    }

    public TodoItem withDone(boolean done) {
        if (this.done == done) {
            return this;
        }
        return new TodoItem(id, done, label);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "TodoItem{" + "id=" + id + ", done=" + done + ", label=" + label + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TodoItem todoItem)) return false;
        return Objects.equals(id, todoItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
