package com.gempukku.lotro.cards.set31.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot a [MORIA] card. Each time you play a weapon, add (1). Response: If an Orc is
 * about to take a wound, discard this condition to prevent that wound.
 */
public class Card31_035 extends AbstractPermanent {
    public Card31_035() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Great Goblin's Power");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 1, Culture.MORIA);
	}

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.and(Filters.owner(self.getOwner()), Filters.weapon))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new AddTwilightEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.ORC)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final Collection<PhysicalCard> cardsToBeWounded = woundEffect.getAffectedCardsMinusPrevented(game);
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an Orc", Race.ORC, Filters.in(cardsToBeWounded)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard woundedOrc) {
                            action.appendEffect(
                                    new PreventCardEffect(woundEffect, woundedOrc));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}