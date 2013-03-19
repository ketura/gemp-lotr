package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 4
 * Orc Stalker
 * Minion • Orc
 * 10	3	6
 * When you play this minion, name a Free Peoples culture.
 * If this minion is assigned to skirmish a companion of the named culture, it is fierce until the regroup phase.
 * http://lotrtcg.org/coreset/sauron/orcstalker(r1).png
 */
public class Card20_371 extends AbstractMinion {
    public Card20_371() {
        super(4, 10, 3, 6, Race.ORC, Culture.SAURON, "Orc Stalker");
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
        if (TriggerConditions.assignedAgainst(game, effectResult, null, Filters.and(CardType.COMPANION, (Culture) self.getWhileInZoneData()), self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
