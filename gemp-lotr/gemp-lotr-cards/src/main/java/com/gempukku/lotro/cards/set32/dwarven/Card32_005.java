package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Ally • Home 9 • Dwarf
 * Strength: 9
 * Vitality: 3
 * Site: 9
 * Game Text: Damage +1. Maneuver: If you can spot more minions than companions, exert Iron Hills Army
 * and discard a Dwarven follower to make an opponent discard a minion.
 */
public class Card32_005 extends AbstractAlly {
    public Card32_005() {
        super(3, SitesBlock.HOBBIT, 9, 9, 3, Race.DWARF, Culture.DWARVEN, "Iron Hills Army", null, true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, self)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.DWARVEN, CardType.FOLLOWER)
                && Filters.countActive(game, CardType.MINION)
                > Filters.countActive(game, CardType.COMPANION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, self));
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.FOLLOWER));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        public void opponentChosen(String opponentId) {
                            action.insertEffect(
                                    new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, 1, 1, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
