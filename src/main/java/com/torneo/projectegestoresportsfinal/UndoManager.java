package com.torneo.projectegestoresportsfinal;

import java.util.Stack;

public class UndoManager {
    private Stack<String> actions;

    public UndoManager() {
        actions = new Stack<>();
    }

    public void addAction(String action) {
        actions.push(action);
    }

    public String undo() {
        if(!actions.isEmpty()){
            return actions.pop();
        }
        return null;
    }
}
