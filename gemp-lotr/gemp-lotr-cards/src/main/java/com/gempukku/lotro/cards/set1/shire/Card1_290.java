package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion � Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Frodo
 * Game Text: Ring-bearer (resistance 10). Fellowship: Exert another companion who has the Frodo signet to heal Frodo.
 */
public class Card1_290 extends AbstractCompanion {
    public Card1_290() {
        super(0, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Frodo", true);
        addKeyword(Keyword.RING_BEARER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION), Filters.signet(Signet.FRODO), Filters.not(Filters.sameCard(self)), Filters.canExert())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert another companion who has the Frodo signet to heal Frodo.");
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose another companion who has the Frodo signet", true, Filters.type(CardType.COMPANION), Filters.signet(Signet.FRODO), Filters.not(Filters.sameCard(self)), Filters.canExert()));
            action.addEffect(
                    new HealCharacterEffect(self));

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public int getResistance() {
        return 10;
    }
}
