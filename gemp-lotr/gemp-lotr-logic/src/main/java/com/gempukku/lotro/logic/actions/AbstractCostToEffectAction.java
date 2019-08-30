package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscountEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public abstract class AbstractCostToEffectAction implements CostToEffectAction {
    private LinkedList<DiscountEffect> _potentialDiscounts = new LinkedList<DiscountEffect>();
    private LinkedList<DiscountEffect> _processedDiscounts = new LinkedList<DiscountEffect>();
    private LinkedList<Effect> _costs = new LinkedList<Effect>();
    private LinkedList<Effect> _processedCosts = new LinkedList<Effect>();
    private LinkedList<Effect> _effects = new LinkedList<Effect>();

    private Phase _actionTimeword;
    private String _performingPlayer;

    private boolean _virtualCardAction = false;
    private boolean _paidToil = false;

    public boolean isPaidToil() {
        return _paidToil;
    }

    private ActionContext actionContext = new DefaultActionContext();

    public void setActionContext(ActionContext actionContext) {
        this.actionContext = actionContext;
    }

    @Override
    public void setValueToMemory(String memory, String value) {
        actionContext.setValueToMemory(memory, value);
    }

    @Override
    public String getValueFromMemory(String memory) {
        return actionContext.getValueFromMemory(memory);
    }

    @Override
    public void setCardMemory(String memory, PhysicalCard card) {
        actionContext.setCardMemory(memory, card);
    }

    @Override
    public void setCardMemory(String memory, Collection<? extends PhysicalCard> cards) {
        actionContext.setCardMemory(memory, cards);
    }

    @Override
    public Collection<? extends PhysicalCard> getCardsFromMemory(String memory) {
        return actionContext.getCardsFromMemory(memory);
    }

    @Override
    public PhysicalCard getCardFromMemory(String memory) {
        return actionContext.getCardFromMemory(memory);
    }

    @Override
    public void setPaidToil(boolean paidToil) {
        _paidToil = paidToil;
    }

    @Override
    public void setVirtualCardAction(boolean virtualCardAction) {
        _virtualCardAction = virtualCardAction;
    }

    @Override
    public boolean isVirtualCardAction() {
        return _virtualCardAction;
    }

    @Override
    public Phase getActionTimeword() {
        return _actionTimeword;
    }

    @Override
    public void setActionTimeword(Phase phase) {
        _actionTimeword = phase;
    }

    @Override
    public void setPerformingPlayer(String playerId) {
        _performingPlayer = playerId;
    }

    @Override
    public String getPerformingPlayer() {
        return _performingPlayer;
    }

    @Override
    public final void appendPotentialDiscount(DiscountEffect discount) {
        _potentialDiscounts.add(discount);
    }

    @Override
    public final void appendCost(Effect cost) {
        _costs.add(cost);
    }

    @Override
    public final void appendEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public final void insertCost(Effect... cost) {
        _costs.addAll(0, Arrays.asList(cost));
    }

    @Override
    public final void insertEffect(Effect... effect) {
        _effects.addAll(0, Arrays.asList(effect));
    }

    protected boolean isCostFailed() {
        for (Effect processedCost : _processedCosts) {
            if (!processedCost.wasCarriedOut())
                return true;
        }
        return false;
    }

    protected int getProcessedDiscount() {
        int discount = 0;
        for (DiscountEffect processedDiscount : _processedDiscounts) {
            discount += processedDiscount.getDiscountPaidFor();
        }
        return discount;
    }

    protected int getPotentialDiscount(LotroGame game) {
        int sum = 0;
        for (DiscountEffect potentialDiscount : _potentialDiscounts) {
            sum += potentialDiscount.getMaximumPossibleDiscount(game);
        }
        return sum;
    }

    protected final Effect getNextCost() {
        Effect cost = _costs.poll();
        if (cost != null)
            _processedCosts.add(cost);
        return cost;
    }

    protected final Effect getNextEffect() {
        return _effects.poll();
    }

    protected final DiscountEffect getNextPotentialDiscount() {
        DiscountEffect discount = _potentialDiscounts.poll();
        if (discount != null)
            _processedDiscounts.add(discount);
        return discount;
    }
}
