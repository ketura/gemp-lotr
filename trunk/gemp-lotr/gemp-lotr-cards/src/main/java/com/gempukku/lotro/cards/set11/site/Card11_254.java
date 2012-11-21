package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Plains. At the start of the maneuver phase, the Free Peoples player must add a burden or discard 3 cards
 * from hand.
 */
public class Card11_254 extends AbstractNewSite {
    public Card11_254() {
        super("Pelennor Flat", 1, Direction.RIGHT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            String fpPlayerId = game.getGameState().getCurrentPlayerId();
            possibleEffects.add(
                    new ChooseAndDiscardCardsFromHandEffect(action, game.getGameState().getCurrentPlayerId(), false, 3) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard 3 cards from hand";
                        }
                    });
            possibleEffects.add(
                    new AddBurdenEffect(fpPlayerId, self, 1));
            action.appendEffect(
                    new ChoiceEffect(action, fpPlayerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
