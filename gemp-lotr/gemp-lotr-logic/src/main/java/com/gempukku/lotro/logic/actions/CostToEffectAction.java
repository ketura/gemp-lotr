package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.DiscountEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;

public interface CostToEffectAction extends Action {
    void appendPotentialDiscount(DiscountEffect cost);

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

    void setPaidToil(boolean toilPaid);

    void setValueToMemory(String memory, String value);

    String getValueFromMemory(String memory);

    void setCardMemory(String memory, PhysicalCard card);

    void setCardMemory(String memory, Collection<? extends PhysicalCard> cards);

    Collection<? extends PhysicalCard> getCardsFromMemory(String memory);

    PhysicalCard getCardFromMemory(String memory);
}
