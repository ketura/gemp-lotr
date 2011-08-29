package com.gempukku.lotro.logic.timing;

import java.util.Stack;

public class ActionStack {
    private Stack<Action> _actionStack = new Stack<Action>();

    public void stackAction(Action action) {
        _actionStack.add(action);
    }

    public Effect getNextEffect() {
        Action action = _actionStack.peek();
        Effect effect = action.nextEffect();
        if (effect != null) {
            return effect;
        } else {
            _actionStack.pop();
            return null;
        }
    }

    public boolean isEmpty() {
        return _actionStack.isEmpty();
    }
}
