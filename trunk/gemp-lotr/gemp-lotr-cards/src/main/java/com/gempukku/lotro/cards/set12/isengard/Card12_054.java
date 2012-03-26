package com.gempukku.lotro.cards.set12.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Wizard
 * Strength: 8
 * Vitality: 4
 * Site: 4
 * Game Text: Damage +1. Fierce. Lurker. (Skirmishes involving lurker minions must be resolved after any others.) When
 * you play Saruman, name a culture. Each companion of the named culture is strength -1.
 */
public class Card12_054 extends AbstractMinion {
    public Card12_054() {
        super(4, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.LURKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(CardType.COMPANION,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return self.getWhileInZoneData() != null && physicalCard.getBlueprint().getCulture() == (Culture) self.getWhileInZoneData();
                    }
                }), -1);
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
        return null;
    }
}
