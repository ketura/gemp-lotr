package com.gempukku.lotro.cards.set9.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Artifact • Support Area
 * Game Text: To play, spot a [GONDOR] Man with 3 or more vitality (or spot a [GONDOR] Man and add 2 threats).
 * Regroup: Exert a [GONDOR] Man or discard this artifact to discard your hand and draw 3 cards.
 */
public class Card9_037 extends AbstractPermanent {
    public Card9_037() {
        super(Side.FREE_PEOPLE, 0, CardType.ARTIFACT, Culture.GONDOR, "Seeing Stone of Minas Anor", null, true);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractExtraPlayCostModifier(self, "Extra cost to play", self) {
                    @Override
                    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
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
                                    new AddThreatsEffect(self.getOwner(), self, 2) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Spot a GONDOR Man and add 2 threats";
                                        }
                                    });
                        }
                        action.appendCost(
                                new ChoiceEffect(action, self.getOwner(), possibleCosts));
                    }

                    @Override
                    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                        return (PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN, Filters.moreVitalityThan(2))
                                || (PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN) && PlayConditions.canAddThreat(game, self, 2)));
                    }
                });
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && (PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN) || PlayConditions.canSelfDiscard(self, game))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert a GONDOR Man";
                        }
                    });
            possibleCosts.add(
                    new SelfDiscardEffect(self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this artifact";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new DiscardCardsFromHandEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId)), false));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 3));
            return Collections.singletonList(action);
        }
        return null;
    }
}
