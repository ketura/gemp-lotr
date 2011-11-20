package com.gempukku.lotro.cards.set9.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Artifact • Support Area
 * Game Text: To play, spot a [GONDOR] Man with 3 or more vitality (or spot a [GONDOR] Man and add 2 threats).
 * Regroup: Add a threat or discard this artifact to remove (2) or to draw a card.
 */
public class Card9_038 extends AbstractPermanent {
    public Card9_038() {
        super(Side.FREE_PEOPLE, 0, CardType.ARTIFACT, Culture.GONDOR, Zone.SUPPORT, "Seeing Stone of Orthanc", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && (PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN, Filters.moreVitalityThan(2))
                || (PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN) && PlayConditions.canAddThreat(game, self, 2)));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new SpotEffect(1, Culture.GONDOR, Race.MAN, Filters.moreVitalityThan(2)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Spot a GONDOR Man with 3 or more vitality";
                    }
                });
        if (PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN)) {
            possibleCosts.add(
                    new AddThreatsEffect(playerId, self, 2) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Spot a GONDOR Man and add 2 threats";
                        }
                    });
        }
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));

        return action;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && (PlayConditions.canSelfDiscard(self, game) || PlayConditions.canAddThreat(game, self, 1))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new AddThreatsEffect(playerId, self, 1));
            possibleCosts.add(
                    new DiscardCardsFromPlayEffect(self, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this artifact";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            List<Effect> possibleEffect = new LinkedList<Effect>();
            possibleEffect.add(
                    new RemoveTwilightEffect(2));
            possibleEffect.add(
                    new DrawCardEffect(playerId, 1));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffect));
            return Collections.singletonList(action);
        }
        return null;
    }
}
