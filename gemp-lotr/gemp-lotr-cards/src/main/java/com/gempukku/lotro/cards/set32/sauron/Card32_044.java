package com.gempukku.lotro.cards.set32.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Vitality: +1
 * Game Text: Bearer must be Sauron. He is fierce. Skirmish: Remove (2) to make
 * Sauron or a Wraith minion damage +1 (limit +1).
 */

public class Card32_044 extends AbstractAttachable {
    public Card32_044() {
        super(Side.SHADOW, CardType.ARTIFACT, 0, Culture.SAURON, PossessionClass.RING, "Ring of Thror", "Last of the Seven Rings", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Sauron");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));

        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 2)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", Filters.or(Filters.name("Sauron"), Filters.and(Culture.WRAITH, CardType.MINION))) {
                        @Override
                        public void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new CheckPhaseLimitEffect(action, self, "dmg", 1, Phase.SKIRMISH,
                                            new AddUntilEndOfPhaseModifierEffect(
                                                    new KeywordModifier(self, card, Keyword.DAMAGE))));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
