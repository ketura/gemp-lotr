package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Title: *Barad-dur Captain
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 5
 * Type: Minion - Orc
 * Strength: 11
 * Vitality: 3
 * Home: 6
 * Card Number: 1R213
 * Game Text: When you play this minion, name a Free Peoples culture.
 * While skirmishing a character of the named culture, this minion is damage +1.
 * Skirmish: Exert Barad-dur Captain to wound a character it is skirmishing.
 */
public class Card40_213 extends AbstractMinion {
    public Card40_213() {
        super(5, 11, 3, 6, Race.ORC, Culture.SAURON, "Barad-dur Captain", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<String> possibleCultures = new LinkedList<String>();
            for (Culture value : Culture.values()) {
                if (value.isFP())
                    possibleCultures.add(value.getHumanReadable());
            }
            final String[] cultures = possibleCultures.toArray(new String[0]);

            action.appendEffect(
                    new PlayoutDecisionEffect(self.getOwner(),
                            new MultipleChoiceAwaitingDecision(1, "Choose a Free Peoples Culture", cultures) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    final Culture culture = Culture.findCultureByHumanReadable(result);
                                    action.appendEffect(
                                            new AddUntilEndOfTurnModifierEffect(
                                                    new KeywordModifier(self, self, new SpotCondition(self, Filters.inSkirmishAgainst(Filters.character, culture)), Keyword.DAMAGE, 1)));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.character, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
