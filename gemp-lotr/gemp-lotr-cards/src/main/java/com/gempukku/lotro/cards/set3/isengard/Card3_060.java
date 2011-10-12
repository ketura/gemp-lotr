package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 3
 * Site: 4
 * Game Text: Regroup: Exert this minion to discard a weapon (or all Free Peoples possessions if you can spot
 * 5 companions).
 */
public class Card3_060 extends AbstractMinion {
    public Card3_060() {
        super(4, 8, 3, 4, Race.ORC, Culture.ISENGARD, "Isengard Smith");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            int characterSpot = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION));
            if (characterSpot >= 5) {
                action.appendEffect(
                        new DiscardCardsFromPlayEffect(self, Filters.and(Filters.side(Side.FREE_PEOPLE), Filters.type(CardType.POSSESSION))));
            } else {
                action.appendEffect(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.weapon()));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
