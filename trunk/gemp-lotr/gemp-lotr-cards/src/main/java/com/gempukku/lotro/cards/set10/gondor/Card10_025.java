package com.gempukku.lotro.cards.set10.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 5
 * Type: Companion • Man
 * Strength: 9
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: When you play Aragorn, you may heal another companion. At the start of each fellowship phase, you may
 * exert a companion of one culture to heal a companion of another culture.
 */
public class Card10_025 extends AbstractCompanion {
    public Card10_025() {
        super(5, 9, 4, 6, Culture.GONDOR, Race.MAN, Signet.FRODO, "Aragorn", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.not(self), CardType.COMPANION));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && PlayConditions.canExert(self, game, CardType.COMPANION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(character.getBlueprint().getCulture())));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
