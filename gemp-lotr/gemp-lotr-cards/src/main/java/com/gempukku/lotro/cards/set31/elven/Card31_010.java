package com.gempukku.lotro.cards.set31.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally • Home 5 • Elf
 * Strength: 6
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Maneuver: Exert Tauriel to return an Orc with strength 7 or less to its owner's hand.
 */
public class Card31_010 extends AbstractAlly {
    public Card31_010() {
        super(2, Block.HOBBIT, 5, 6, 3, Race.ELF, Culture.ELVEN, "Tauriel", "Staunch Defender", true);
		addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Race.ORC, Filters.lessStrengthThan(8)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
