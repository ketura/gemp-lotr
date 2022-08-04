package com.gempukku.lotro.cards.set12.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Mount
 * Strength: +1
 * Resistance: +1
 * Game Text: Bearer must be a knight. Bearer is damage +1. Each time the fellowship moves from a mountain site,
 * add (2).
 */
public class Card12_049 extends AbstractAttachableFPPossession {
    public Card12_049() {
        super(1, 1, 0, Culture.GONDOR, PossessionClass.MOUNT, "Gondorian Steed");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.KNIGHT;
    }

    @Override
    public int getResistance() {
        return 1;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Keyword.MOUNTAIN)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
