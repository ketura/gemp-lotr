package com.gempukku.lotro.cards.set17.gondor;
import java.util.List;
import java.util.Collections;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Bearer must be a [GONDOR] companion. An opponent can not play skirmish events or use skirmish special
 * abilities during skirmishes involving bearer. Each time a Shadow player adds a burden, discard this condition.
 */
public class Card17_034 extends AbstractAttachable {
    public Card17_034() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GONDOR, null, "Spirit of the White Tree");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(self,
new SpotCondition(Filters.hasAttached(self), Filters.inSkirmish), Side.SHADOW, Phase.SKIRMISH));
}

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.sidePlayerAddedBurden(game, effectResult, Side.SHADOW)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
