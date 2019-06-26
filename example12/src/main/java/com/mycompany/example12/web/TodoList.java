package com.mycompany.example12.web;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class TodoList implements Iterable<TodoItem>, Serializable {

    private static final long serialVersionUID = 1L;
    private final Set<TodoItem> items;

    public TodoList() {
        items = Collections.synchronizedSet(new LinkedHashSet<>());
    }

    public void addItem(TodoItem item) {
        this.items.add(item);
    }

    public void removeById(UUID id) {
        synchronized (items) {
            for (Iterator<TodoItem> iterator = items.iterator(); iterator.hasNext();) {
                final TodoItem item = iterator.next();
                if (id.equals(item.getId())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public void setDoneById(UUID id, boolean done) {
        synchronized (items) {
            for (Iterator<TodoItem> iterator = items.iterator(); iterator.hasNext();) {
                final TodoItem item = iterator.next();
                if (id.equals(item.getId())) {
                    if (item.isDone() != done) {
                        iterator.remove();
                        this.items.add(item.withDone(done));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public Iterator<TodoItem> iterator() {
        return Collections.unmodifiableCollection(items).iterator();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.items);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TodoList other = (TodoList) obj;
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        return true;
    }

}
