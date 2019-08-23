package com.gempukku.lotro.cards.set31.spider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Spider
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, spot a Spider. Assignment: Discard an Orc from play to play a spider from your
 * draw deck or discard pile. Its twilight cost is -2 (or -4 at a forest).
 */
public class Card31_062 extends AbstractPermanent {
    public Card31_062() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GUNDABAD, "Spider Nest");
    }


    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.SPIDER);
    }


    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, Race.ORC)) {
            int reduction = Filters.and(Keyword.FOREST).accepts(game, game.getGameState().getCurrentSite()) ? -4 : -2;

            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.ORC));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, reduction, Race.SPIDER) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play a Spider from your draw deck";
                        }
                    });
            if (PlayConditions.canPlayFromDiscard(playerId, game, reduction, Race.SPIDER)) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, reduction, Race.SPIDER) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Play a Spider from your discard pile";
                            }
                        });
            }
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}