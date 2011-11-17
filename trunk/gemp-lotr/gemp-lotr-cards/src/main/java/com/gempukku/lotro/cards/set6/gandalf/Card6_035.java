package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 7
 * Type: Companion • Ent
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Game Text: Skinbark's twilight cost is -1 for each Ent or unbound Hobbit you can spot. Response: If an unbound
 * Hobbit is about to take a wound, exert Skinbark to prevent that wound.
 */
public class Card6_035 extends AbstractCompanion {
    public Card6_035() {
        super(7, 8, 4, Culture.GANDALF, Race.ENT, null, "Skinbark", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countSpottable(gameState, modifiersQuerying, Filters.or(Race.ENT, Filters.and(Race.HOBBIT, Filters.unboundCompanion)));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.HOBBIT, Filters.unboundCompanion)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final Collection<PhysicalCard> cardsToBeWounded = woundEffect.getAffectedCardsMinusPrevented(game);
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound Hobbit", Race.HOBBIT, Filters.unboundCompanion, Filters.in(cardsToBeWounded)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard unboundHobbit) {
                            action.appendEffect(
                                    new PreventCardEffect(woundEffect, unboundHobbit));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
