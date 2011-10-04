package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

public interface CostToEffectAction extends Action {
    /**
     * Inserts the specified costs as the next costs to be executed.
     *
     * @param cost
     */
    public void insertCost(Effect... cost);

    /**
     * Appends the specified cost to the list of the costs. It will be executed after all the other costs currently in
     * the queue.
     *
     * @param cost
     */
    public void appendCost(Effect cost);

    /**
     * Inserts the speicified effects as the next effects to be executer.
     *
     * @param effect
     */
    public void insertEffect(Effect... effect);

    /**
     * Appends the specified effect to the list of the effects. It will be executed after all the other costs currently
     * in the queue.
     *
     * @param effect
     */
    public void appendEffect(Effect effect);
}
