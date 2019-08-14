package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.ExtraFilters;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Ulaire Lemenya, Shadowy Wraith
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 4
 * Type: Minion - Nazgul
 * Strength: 9
 * Vitality: 2
 * Home: 3
 * Card Number: 1U206
 * Game Text: When you play Ulaire Lemenya, you may spot a companion with 3 or less resistance to play a mount from your draw deck on a Nazgul.
 */
public class Card40_206 extends AbstractMinion {
    public Card40_206() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, Names.lemenya, "Shadowy Wraith", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.maxResistance(3))
                && PlayConditions.canPlayFromDeck(playerId, game, PossessionClass.MOUNT, ExtraFilters.attachableTo(game, Race.NAZGUL))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId),
                            Filters.and(
                                    PossessionClass.MOUNT,
                                    ExtraFilters.attachableTo(game, Race.NAZGUL)), 1, 1, true) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, 0, Race.NAZGUL, false));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
