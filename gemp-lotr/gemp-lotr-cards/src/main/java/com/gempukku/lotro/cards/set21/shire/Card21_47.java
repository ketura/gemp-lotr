package com.gempukku.lotro.cards.set21.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Artifact • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Bilbo. Each time Bilbo wins a skirmish in which you played a [DWARVEN] event, you may 
 * discard a condition.
 */
 
public class Card21_47 extends AbstractAttachableFPPossession {
    public Card21_47() {
        super(1, 2, 0, Culture.SHIRE, CardType.ARTIFACT, PossessionClass.HAND_WEAPON, "Sting", null, true);
    }

	@Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Bilbo");
	}
	
    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())
                && Filters.filter(game.getActionsEnvironment().getPlayedCardsInCurrentPhase(), game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Culture.DWARVEN, CardType.EVENT).size() > 0) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
			action.setText("Choose a condition");
            action.appendEffect(
					new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}