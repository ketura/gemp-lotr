package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 5
 * •Barad-Dur Captain
 * Minion • Orc
 * 11	3	6
 * When you play this minion, name a Free Peoples culture.
 * While skirmishing a character of the named culture, this minion is damage +1.
 * Skirmish: Exert Barad-Dur Captain to wound a character it is skirmishing.
 * http://lotrtcg.org/coreset/sauron/baraddurcaptain(r1).png
 */
public class Card20_349 extends AbstractMinion {
    public Card20_349() {
        super(5, 11, 3, 6, Race.ORC, Culture.SAURON, "Barad-Dûr Captain", null, true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, self,
                new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        Culture chosenCulture = (Culture) self.getWhileInZoneData();
                        if (chosenCulture == null)
                            return false;
                        return Filters.inSkirmishAgainst(chosenCulture).accepts(game, self);
                    }
                }, Keyword.DAMAGE, 1));
    }

    @Override
    public String getDisplayableInformation(PhysicalCard self) {
        if (self.getWhileInZoneData() != null)
            return "Chosen culture is " + ((Culture) self.getWhileInZoneData()).getHumanReadable();
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            if (TriggerConditions.played(game, effectResult, self)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);

                final Set<String> possibleCultures = new LinkedHashSet<String>();
                for (Culture culture : Culture.values())
                    if (culture.isFP())
                        possibleCultures.add(culture.getHumanReadable());

                action.appendEffect(
                        new PlayoutDecisionEffect(self.getOwner(),
                                new MultipleChoiceAwaitingDecision(1, "Choose a culture", possibleCultures.toArray(new String[possibleCultures.size()])) {
                                    @Override
                                    protected void validDecisionMade(int index, String result) {
                                        self.setWhileInZoneData(Culture.findCultureByHumanReadable(result));
                                    }
                                }));
                return Collections.singletonList(action);
            }
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
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
