package com.gempukku.lotro.cards.set31.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Minion • Orc
 * Strength: 5
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may place an Orc (or two Orcs at an underground site)
 * from your discard pile beneath your draw deck.
 */
public class Card31_032 extends AbstractMinion {
    public Card31_032() {
        super(1, 5, 1, 4, Race.ORC, Culture.MORIA, "Goblin Sneak");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
			int checkUnderground = game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.UNDERGROUND) ? 2 : 1;
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose Orc (or two if at an underground site)", game.getGameState().getDiscard(playerId), Race.ORC, checkUnderground, checkUnderground) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.appendEffect(
                                        new PutCardFromDiscardOnBottomOfDeckEffect(selectedCard));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}