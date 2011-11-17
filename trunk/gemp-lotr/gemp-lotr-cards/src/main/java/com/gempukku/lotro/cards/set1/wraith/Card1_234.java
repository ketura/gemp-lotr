package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: When you play Ulaire Nertea, for each companion over 4, you may play 1 minion from your discard pile.
 */
public class Card1_234 extends AbstractMinion {
    public Card1_234() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Nertea", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.sameCard(self))
                // You must be able to play a minion from discard to use this trigger
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int companions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
            int minions = Math.max(0, companions - 4);
            for (int i = 0; i < minions; i++) {
                action.appendEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), CardType.MINION));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
