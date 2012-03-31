package com.gempukku.lotro.cards.set19.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.modifiers.MayNotBearModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion • Creature
 * Strength: 11
 * Vitality: 4
 * Site: 4
 * Game Text: Discard all other minions (except tentacles). Watcher in the Water cannot bear possessions.
 * The fellowship's current site gains marsh. Shadow: Play a tentacle from your discard pile.
 */
public class Card19_021 extends AbstractMinion {
    public Card19_021() {
        super(4, 11, 4, 4, Race.CREATURE, Culture.MORIA, "Watcher in the Water", "Many-Tentacled Creature", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.canSpot(game, CardType.MINION, Filters.not(Keyword.TENTACLE), Filters.not(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, CardType.MINION, Filters.not(Keyword.TENTACLE), Filters.not(self)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MayNotBearModifier(self, self, CardType.POSSESSION));
        modifiers.add(
                new KeywordModifier(self, Filters.currentSite, Keyword.MARSH));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canPlayFromDiscard(playerId, game, Keyword.TENTACLE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Keyword.TENTACLE));
            return Collections.singletonList(action);
        }
        return null;
    }
}
