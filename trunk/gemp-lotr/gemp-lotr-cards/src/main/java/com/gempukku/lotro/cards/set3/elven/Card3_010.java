package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally • Home 6 • Elf
 * Strength: 3
 * Vitality: 3
 * Site: 6
 * Game Text: Archer. Regroup: Exert Calaglin to wound a minion.
 */
public class Card3_010 extends AbstractAlly {
    public Card3_010() {
        super(2, 6, 3, 3, Race.ELF, Culture.ELVEN, "Calaglin", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.REGROUP);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.type(CardType.MINION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
