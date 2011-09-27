package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
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
        super(2, Culture.DWARVEN, Keyword.HAND_WEAPON, "Gimli's Battle Axe", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Gimli");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 2));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));
        return new CompositeModifier(self, Filters.hasAttached(self), modifiers);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Orc to wound", Filters.race(Race.ORC)) {
                        @Override
                        protected void cardSelected(PhysicalCard orc) {
                            action.appendEffect(new CardAffectsCardEffect(self, orc));
                            action.appendEffect(new WoundCharacterEffect(playerId, orc));
                        }
                    }
            );

            return Collections.singletonList(action);
        }

        return null;
    }
}
