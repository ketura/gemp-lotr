package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.MayNotBearModifier;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion • Creature
 * Strength: 11
 * Vitality: 4
 * Site: 4
 * Game Text: Damage +1. While you can spot Watcher in the Water, discard all other minions (except tentacles). Each
 * tentacle is strength +2 and damage +1. This minion may not bear possessions and is discarded if not at a marsh.
 */
public class Card2_073 extends AbstractMinion {
    public Card2_073() {
        super(4, 11, 4, 4, Race.CREATURE, Culture.MORIA, "Watcher in the Water", true);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.keyword(Keyword.TENTACLE), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.keyword(Keyword.TENTACLE), Keyword.DAMAGE));
        modifiers.add(
                new MayNotBearModifier(self, Filters.sameCard(self), Filters.type(CardType.POSSESSION)));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.MINION), Filters.not(Filters.sameCard(self)), Filters.not(Filters.keyword(Keyword.TENTACLE)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, Filters.and(Filters.type(CardType.MINION), Filters.not(Filters.sameCard(self)), Filters.not(Filters.keyword(Keyword.TENTACLE)))));
            actions.add(action);
        }
        if (!game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.MARSH)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            actions.add(action);
        }
        return actions;
    }
}
