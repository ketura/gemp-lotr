package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealCardEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 2
 * Site: 4
 * Game Text: At the start of each skirmish involving this minion, you may reveal an [ORC] condition from hand to wound
 * the companion this minion is skirmishing twice and then discard this minion.
 */
public class Card17_079 extends AbstractMinion {
    public Card17_079() {
        super(4, 10, 2, 4, Race.ORC, Culture.ORC, "Orkish Invader");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.ORC, CardType.CONDITION).size() > 0) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseCardsFromHandEffect(playerId, 1, 1, Culture.ORC, CardType.CONDITION) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.appendCost(
                                        new RevealCardEffect(self, selectedCard));
                            }
                        }
                    });
            action.appendEffect(
                    new WoundCharactersEffect(self, CardType.COMPANION, Filters.inSkirmishAgainst(self)));
            action.appendEffect(
                    new WoundCharactersEffect(self, CardType.COMPANION, Filters.inSkirmishAgainst(self)));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
