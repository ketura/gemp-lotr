package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot 3 Free Peoples cultures or discard 2 [ORC] conditions from play.
 */
public class Card15_107 extends AbstractMinion {
    public Card15_107() {
        super(2, 12, 3, 4, Race.ORC, Culture.ORC, "Desolation Orc");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractExtraPlayCostModifier(self, "Extra cost to play", self, null) {
                    @Override
                    public void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card) {
                        List<Effect> possibleCosts = new LinkedList<>();
                        if (new CountCulturesEvaluator(Side.FREE_PEOPLE).evaluateExpression(game, null) >= 3)
                            possibleCosts.add(
                                    new UnrespondableEffect() {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Spot 3 Free Peoples cultures";
                                        }

                                        @Override
                                        protected void doPlayEffect(LotroGame game) {
                                        }
                                    });
                        possibleCosts.add(
                                new ChooseAndDiscardCardsFromPlayEffect(action, self.getOwner(), 2, 2, Culture.ORC, CardType.CONDITION));
                        action.appendCost(
                                new ChoiceEffect(action, self.getOwner(), possibleCosts));
                    }

                    @Override
                    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card) {
                        return (new CountCulturesEvaluator(Side.FREE_PEOPLE).evaluateExpression(game, null) >= 3
                                || PlayConditions.canDiscardFromPlay(self, game, 2, Culture.ORC, CardType.CONDITION));
                    }
                });
    }
}
