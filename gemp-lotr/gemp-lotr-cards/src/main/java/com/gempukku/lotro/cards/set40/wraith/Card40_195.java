package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Title: *The Pale Blade, Weapon of Woe
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 2
 * Type: Possession - Hand Weapon
 * Strength: +3
 * Card Number: 1R195
 * Game Text: Bearer must be a Nazgul. If bearer is The Witch-king, he is damage +1.
 * Each time The Witch-king wins a skirmish, you may discard a Free Peoples condition.
 */
public class Card40_195 extends AbstractAttachable {
    public Card40_195() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, PossessionClass.HAND_WEAPON, "The Pale Blade", "Weapon of Woe", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier strength = new StrengthModifier(self, Filters.hasAttached(self), 3);
        KeywordModifier damage = new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.witchKing), Keyword.DAMAGE, 1);
        return Arrays.asList(strength, damage);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.witchKing)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
