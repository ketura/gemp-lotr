package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Follower
 * Strength: +2
 * Vitality: +1
 * Game Text: Aid - Exert Gandalf. Shadow cards cannot discard Beorn.
 * Each time bearer wins a skirmish, you may take a [GANDALF] card from your
 * discard pile into hand.
 */
public class Card31_013 extends AbstractFollower {
    public Card31_013() {
        super(Side.FREE_PEOPLE, 3, 2, 1, 0, Culture.GANDALF, "Beorn", "Skin-Changer", true);
    }
	
    @Override
    public Race getRace() {
        return Race.MAN;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CantDiscardFromPlayModifier(self, "Cannot be discarded from play by Shadow cards",
                new SpotCondition(Filters.hasAttached(self)), self, Side.SHADOW));
    }
	
    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
	return PlayConditions.canExert(self, game, Filters.gandalf);		
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
         return new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Filters.gandalf);
    }

    @Override
    protected List<OptionalTriggerAction> getExtraOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
