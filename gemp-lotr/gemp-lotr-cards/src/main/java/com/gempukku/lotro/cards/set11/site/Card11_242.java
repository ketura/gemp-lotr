package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 3
 * Type: Site
 * Game Text: Dwelling. At the start of your fellowship phase, you may exert 2 Hobbits to play a Hobbit from your draw
 * deck.
 */
public class Card11_242 extends AbstractNewSite {
    public Card11_242() {
        super("Green Dragon Inn", 3, Direction.RIGHT);
        addKeyword(Keyword.DWELLING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && playerId.equals(game.getGameState().getCurrentPlayerId())
                && PlayConditions.canExert(self, game, 1, 2, Race.HOBBIT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.HOBBIT));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Race.HOBBIT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
