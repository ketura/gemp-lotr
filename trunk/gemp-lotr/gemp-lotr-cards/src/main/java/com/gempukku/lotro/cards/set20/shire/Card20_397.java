package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
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
 * 1
 * •Merry, Pipeweed Aficionado
 * Shire	Companion • Hobbit
 * 3 	4	8
 * Add 1 to the number of pipes you can spot.
 * At the start of the fellowship phase, you may exert Merry to play a pipeweed possession from your draw deck.
 */
public class Card20_397 extends AbstractCompanion {
    public Card20_397() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Merry", "Pipeweed Aficionado", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new PossessionClassSpotModifier(self, PossessionClass.PIPE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && PlayConditions.canSelfExert(self, game)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, CardType.POSSESSION, Keyword.PIPEWEED));
            return Collections.singletonList(action);
        }
        return null;
    }
}
