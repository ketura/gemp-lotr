package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition • Support area
 * Game Text: If you cannot spot 2 Wise characters, each [WRAITH] minion is strength +2. Shadow: Discard an Orc
 * from hand to play a [WRAITH] minion from your discard pile.
 */
public class Card32_066 extends AbstractPermanent {
    public Card32_066() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, "He Is Summoning His Servants", null, true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(final LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Culture.WRAITH, CardType.MINION),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                if (PlayConditions.canSpot(game, 2, Keyword.WISE))
                                    return false;

                                return true;
                            }
                        }, 2));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Race.ORC)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.WRAITH, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Race.ORC));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.WRAITH, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
