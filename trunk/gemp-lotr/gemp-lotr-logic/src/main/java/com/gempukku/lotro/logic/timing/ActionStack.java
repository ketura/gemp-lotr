package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

import java.util.Stack;

public class ActionStack {
    private Stack<Action> _actionStack = new Stack<Action>();

    public void stackAction(Action action) {
        _actionStack.add(action);
    }

    public Effect getNextEffect(LotroGame game) {
        Action action = _actionStack.peek();
        Effect effect = action.nextEffect(game);
        if (effect != null) {
            return effect;
        } else {
            _actionStack.remove(_actionStack.lastIndexOf(action));
            return null;
        }
    }

    public boolean isEmpty() {
        return _actionStack.isEmpty();
    }

    public Action getTopmostAction() {
        if (_actionStack.isEmpty())
            return null;
        return _actionStack.peek();
    }

    public <T extends Action> T findTopmostActionOfType(Class<T> clazz) {
        for (int i = _actionStack.size() - 1; i >= 0; i--) {
            final Action actionAtIndex = _actionStack.get(i);
            if (actionAtIndex.getClass() == clazz)
                return (T) actionAtIndex;
        }
        return null;
    }
}
