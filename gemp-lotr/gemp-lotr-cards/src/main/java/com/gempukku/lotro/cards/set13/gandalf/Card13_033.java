package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
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
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 5
 * Game Text: While Gandalf is the Ring-bearer, each time the fellowship moves, add 3 burdens unless you spot 2 other
 * companions and discard 2 [GANDALF] cards from hand.
 */
public class Card13_033 extends AbstractCompanion {
    public Card13_033() {
        super(4, 7, 4, 5, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "Bearer of Obligation", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId()) == self) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new AddBurdenEffect(self, 3));
            if (PlayConditions.canSpot(game, 2, Filters.not(self), CardType.COMPANION))
                possibleEffects.add(
                        new ChooseAndDiscardCardsFromHandEffect(action, game.getGameState().getCurrentPlayerId(), false, 2, Culture.GANDALF) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard 2 GANDALF cards from hand";
                            }
                        });

            action.appendEffect(
                    new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
