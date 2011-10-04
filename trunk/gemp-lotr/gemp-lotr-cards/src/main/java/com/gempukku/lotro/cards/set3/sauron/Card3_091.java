package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
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
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Regroup: Exert a [SAURON] minion to make the Free Peoples player discard
 * the top card from his or her draw deck.
 */
public class Card3_091 extends AbstractPermanent {
    public Card3_091() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SHADOW_SUPPORT, "His Cruelty and Malice");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.type(CardType.MINION))) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.REGROUP);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.SAURON), Filters.type(CardType.MINION)));
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
