package com.gempukku.lotro.cards.set15.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Battleground. Each time a follower is transferred to a character, the first Shadow player may exert
 * a companion twice.
 */
public class Card15_192 extends AbstractNewSite {
    public Card15_192() {
        super("Isengard Ruined", 1, Direction.RIGHT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        final PlayOrder playOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(playerId, false);
        playOrder.getNextPlayer();
        final String firstShadowPlayer = playOrder.getNextPlayer();
        if (TriggerConditions.transferredCard(game, effectResult, CardType.FOLLOWER, null, Filters.character)
                && playerId.equals(firstShadowPlayer)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
