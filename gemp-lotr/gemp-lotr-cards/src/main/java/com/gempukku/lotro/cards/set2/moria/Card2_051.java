package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 12
 * Type: Minion • Balrog
 * Strength: 17
 * Vitality: 5
 * Site: 4
 * Game Text: Damage +1. Fierce. While you can spot The Balrog, discard all other minions. Skip the archery phase.
 * Discard The Balrog if not underground.
 */
public class Card2_051 extends AbstractMinion {
    public Card2_051() {
        super(12, 17, 5, 4, Race.BALROG, Culture.MORIA, "The Balrog", true);
        addKeyword(Keyword.DAMAGE);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ShouldSkipPhaseModifier(self, Phase.ARCHERY));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.MINION, Filters.not(Filters.sameCard(self)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, Filters.and(CardType.MINION, Filters.not(Filters.sameCard(self)))));
            actions.add(action);
        }
        if (!game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.UNDERGROUND)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            actions.add(action);
        }
        return actions;
    }
}
