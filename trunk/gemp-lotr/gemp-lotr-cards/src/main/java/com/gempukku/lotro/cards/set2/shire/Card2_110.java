package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Fellowship: Play Gandalf or Aragorn; his twilight cost is -2.
 */
public class Card2_110 extends AbstractCompanion {
    public Card2_110() {
        super(1, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.ARAGORN, "Pippin", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.name("Gandalf"), Filters.name("Aragorn")), Filters.playable(game, -2)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Filters.or(Filters.name("Gandalf"), Filters.name("Aragorn")), -2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
