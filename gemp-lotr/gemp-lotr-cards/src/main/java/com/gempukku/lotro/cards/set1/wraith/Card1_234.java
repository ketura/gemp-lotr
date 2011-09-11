package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
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
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "For each companion over 4, you may play 1 minion from your discard pile.");
            int companions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION));
            int minions = Math.min(0, companions - 4);
            for (int i = 0; i < minions; i++) {
                action.addEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, Filters.type(CardType.MINION)));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
