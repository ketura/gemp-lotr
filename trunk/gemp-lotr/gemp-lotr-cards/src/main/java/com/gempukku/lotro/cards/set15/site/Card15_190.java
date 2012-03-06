package com.gempukku.lotro.cards.set15.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Twilight Cost: 3
 * Type: Site
 * Game Text: At the start of the Fellowship phase, you may exert two hunter companions to play a hunter companion
 * from your draw deck.
 */
public class Card15_190 extends AbstractNewSite {
    public Card15_190() {
        super("East Wall of Rohan", 3, Direction.RIGHT);
    }

    @Override
    public List<ActivateCardAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && game.getGameState().getCurrentPlayerId().equals(playerId)
                && PlayConditions.canExert(self, game, 1, 2, CardType.COMPANION, Keyword.HUNTER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, CardType.COMPANION, Keyword.HUNTER));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, CardType.COMPANION, Keyword.HUNTER));
            return Collections.singletonList(action);
        }
        return null;
    }
}
