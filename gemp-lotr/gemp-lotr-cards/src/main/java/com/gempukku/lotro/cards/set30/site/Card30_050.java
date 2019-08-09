package com.gempukku.lotro.cards.set30.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Twilight Cost: 3
 * Type: Site
 * Site: 2
 * Game Text: Forest. Underground. At the start of your fellowship phase, you may play a hand weapon from your draw deck.
 */
public class Card30_050 extends AbstractSite {
    public Card30_050() {
        super("The Troll Hoard", Block.HOBBIT, 2, 3, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
		addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && playerId.equals(game.getGameState().getCurrentPlayerId())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, PossessionClass.HAND_WEAPON));
            return Collections.singletonList(action);
        }
        return null;
    }
}