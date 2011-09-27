package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Lurtz. He is damage +1. Each time Lurtz wins a skirmish, the Free Peoples player must
 * discard the top 2 cards of his or her draw deck.
 */
public class Card2_043 extends AbstractAttachable {
    public Card2_043() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.ISENGARD, Keyword.HAND_WEAPON, "Lurtz's Sword", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Lurtz");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.name("Lurtz"))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
