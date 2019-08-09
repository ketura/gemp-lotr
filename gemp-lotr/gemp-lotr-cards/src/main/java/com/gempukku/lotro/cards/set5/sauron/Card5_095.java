package com.gempukku.lotro.cards.set5.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. While the fellowship is at site 4T, Ring-bound companions skirmishing [SAURON]
 * orcs are strength -1. Maneuver: Spot 2 [SAURON] Orcs to play up to 2 [SAURON] conditions from your discard pile.
 * Discard this condition.
 */
public class Card5_095 extends AbstractPermanent {
    public Card5_095() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Dead Marshes", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self,
                        Filters.and(CardType.COMPANION, Keyword.RING_BOUND, Filters.inSkirmishAgainst(Culture.SAURON, Race.ORC)),
                        new LocationCondition(Filters.siteNumber(4), Filters.siteBlock(SitesBlock.TWO_TOWERS)), -1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSpot(game, 2, Culture.SAURON, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new PlayoutDecisionEffect(playerId,
                            new IntegerAwaitingDecision(1, "How many SAURON conditions do you wish to play from your discard pile?", 0, 2) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int count = getValidatedResult(result);
                                    for (int i = 0; i < count; i++)
                                        action.appendEffect(
                                                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.SAURON, CardType.CONDITION));
				    action.appendEffect(
                                            new SelfDiscardEffect(self));
                                }
                            })
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
