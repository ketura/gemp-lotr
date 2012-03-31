package com.gempukku.lotro.cards.set12.moria;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Artifact • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be The Balrog. It is fierce. When you play this artifact, the Free Peoples player reveals his
 * or her hand and discards all Free Peoples cards from hand that have a twilight cost of 1 or less.
 */
public class Card12_080 extends AbstractAttachable {
    public Card12_080() {
        super(Side.SHADOW, CardType.ARTIFACT, 1, Culture.MORIA, PossessionClass.HAND_WEAPON, "Whip of Many Thongs", "Weapon of Flame and Shadow", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.balrog;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RevealAndChooseCardsFromOpponentHandEffect(action, self.getOwner(), game.getGameState().getCurrentPlayerId(), self, "Free Peoples player hand", Filters.none, 0, 0) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            final Collection<PhysicalCard> cardsToDiscard = Filters.filter(game.getGameState().getHand(game.getGameState().getCurrentPlayerId()), game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE, Filters.or(Filters.printedTwilightCost(1), Filters.printedTwilightCost(0)));
                            action.appendEffect(
                                    new DiscardCardsFromHandEffect(self, game.getGameState().getCurrentPlayerId(), cardsToDiscard, true));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
