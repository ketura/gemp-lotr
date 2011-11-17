package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.MayNotBePlayedOnModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

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
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.GANDALF, null, "Hearken to Me", true);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.gandalf);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();

        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new MayNotBePlayedOnModifier(self, Filters.hasAttached(self), Filters.and(Side.SHADOW, CardType.CONDITION)));

        return modifiers;
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Theoden");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, Filters.and(Side.SHADOW, CardType.CONDITION, Filters.attachedTo(Filters.hasAttached(self)))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
