package com.gempukku.lotro.cards.set8.wraith;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Shadow: Discard this minion to play a [WRAITH] minion from your discard pile. Shadow: Spot 6 companions
 * and discard this minion to play up to 2 [WRAITH] minions from your draw deck; their twilight costs are each -1.
 */
public class Card8_077 extends AbstractMinion {
    public Card8_077() {
        super(3, 8, 2, 4, Race.ORC, Culture.WRAITH, "Morgul Squealer");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)) {
            List<ActivateCardAction> actions = new LinkedList<>();
            if (PlayConditions.canSelfDiscard(self, game)
                    && PlayConditions.canPlayFromDiscard(playerId, game, Culture.WRAITH, CardType.MINION)) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play from discard");
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.WRAITH, CardType.MINION));
                actions.add(action);
            }
            if (PlayConditions.canSelfDiscard(self, game)
                    && PlayConditions.canSpot(game, 6, CardType.COMPANION)) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play from deck");
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "How many WRAITH minions do you wish to play from your draw deck?", 0, 2) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int count = getValidatedResult(result);
                                    for (int i = 0; i < count; i++)
                                        action.appendEffect(
                                                new ChooseAndPlayCardFromDeckEffect(playerId, -1, Culture.WRAITH, CardType.MINION));

                                }
                            })
                );
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
