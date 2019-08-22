package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Ally • Home 9 • Dwarf
 * Strength: 7
 * Vitality: 3
 * Site: 9
 * Game Text: Thorin is strength +1. At the start of the regroup phase, you may discard a Dwarven follower
 * to discard a minion (except Smaug).
 */
public class Card32_002 extends AbstractAlly {
    public Card32_002() {
        super(3, SitesBlock.HOBBIT, 9, 7, 3, Race.DWARF, Culture.DWARVEN, "Dain Ironfoot", "Lord of the Iron Hills", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.name("Thorin"), 1));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.DWARVEN, CardType.FOLLOWER)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.FOLLOWER));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION, Filters.not(Filters.name("Smaug"))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
