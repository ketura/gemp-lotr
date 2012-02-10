package com.gempukku.lotro.cards.set17.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Each time you play Saruman, spot a minion. That minion is fierce until the start of the regroup phase.
 * Each Wizard is strength +3 and damage +1.
 */
public class Card17_039 extends AbstractPermanent {
    public Card17_039() {
        super(Side.SHADOW, 2, CardType.ARTIFACT, Culture.ISENGARD, Zone.SUPPORT, "Throne of Isengard", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.saruman)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, self.getOwner(), "Choose a minion", CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Race.WIZARD, 3));
        modifiers.add(
                new KeywordModifier(self, Race.WIZARD, Keyword.DAMAGE, 1));
        return modifiers;
    }
}
