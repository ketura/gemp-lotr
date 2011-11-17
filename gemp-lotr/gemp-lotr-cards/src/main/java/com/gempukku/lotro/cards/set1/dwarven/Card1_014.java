package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession � Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Gimli. He is damage +1. Each time Gimli wins a skirmish, you may wound an Orc.
 */
public class Card1_014 extends AbstractAttachableFPPossession {
    public Card1_014() {
        super(2, 2, 0, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Gimli's Battle Axe", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gimli;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Race.ORC));

            return Collections.singletonList(action);
        }

        return null;
    }
}
