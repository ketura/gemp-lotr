package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.MayNotBePlayedOnModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Condition
 * Strength: +2
 * Vitality: +1
 * Game Text: Spell. To play, spot Gandalf. Bearer must be Theoden.
 * Discard all Shadow conditions on Theoden. Shadow conditions may not be played on Theoden.
 */
public class Card4_094 extends AbstractAttachable {
    public Card4_094() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.GANDALF, null, "Hearken to Me", null, true);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public int getVitality() {
        return 1;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MayNotBePlayedOnModifier(self, Filters.hasAttached(self), Filters.and(Side.SHADOW, CardType.CONDITION)));

        return modifiers;
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name(Names.theoden);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self, Filters.and(Side.SHADOW, CardType.CONDITION, Filters.attachedTo(Filters.hasAttached(self)))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
