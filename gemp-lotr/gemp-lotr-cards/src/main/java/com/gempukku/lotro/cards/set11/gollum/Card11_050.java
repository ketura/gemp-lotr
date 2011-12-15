package com.gempukku.lotro.cards.set11.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: When you play this condition, name forest, mountain, plains, or river. Each time the fellowship moves to
 * a site that has the named keyword, you may exert Gollum or Smeagol to remove (4).
 */
public class Card11_050 extends AbstractPermanent {
    public Card11_050() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Safe Passage", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new PlayoutDecisionEffect(self.getOwner(),
                            new MultipleChoiceAwaitingDecision(1, "Choose type", new String[]{"forest", "mountain", "plains", "river"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (index == 0)
                                        self.setWhileInZoneData(Keyword.FOREST);
                                    else if (index == 1)
                                        self.setWhileInZoneData(Keyword.MOUNTAIN);
                                    else if (index == 2)
                                        self.setWhileInZoneData(Keyword.PLAINS);
                                    else
                                        self.setWhileInZoneData(Keyword.RIVER);
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, (Keyword) self.getWhileInZoneData())
                && PlayConditions.canExert(self, game, Filters.gollumOrSmeagol)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gollumOrSmeagol));
            action.appendEffect(
                    new RemoveTwilightEffect(4));
            return Collections.singletonList(action);
        }
        return null;
    }
}
