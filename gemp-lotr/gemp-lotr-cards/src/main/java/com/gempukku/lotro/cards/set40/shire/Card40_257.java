package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.modifiers.PossessionClassSpotModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Merry, Pipeweed Aficionado
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion - Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1R257
 * Game Text: Add 1 to the number of pipes you can spot. At the start of each of your turns, you may exert Merry twice
 * to play a pipeweed possession from your draw deck.
 */
public class Card40_257 extends AbstractCompanion {
    public Card40_257() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Merry",
                "Pipeweed Aficionado", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        PossessionClassSpotModifier modifier = new PossessionClassSpotModifier(self, PossessionClass.PIPE);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)
                && PlayConditions.canSelfExert(self, 2, game)
                && PlayConditions.canPlayFromDeck(playerId, game, CardType.POSSESSION, Keyword.PIPEWEED)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, CardType.POSSESSION, Keyword.PIPEWEED));
            return Collections.singletonList(action);
        }
        return null;
    }
}
