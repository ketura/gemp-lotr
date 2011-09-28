package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion • Creature
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: Tentacle. Damage +1. When you play this minion, you may play Watcher in the Water from your draw deck.
 * This minion may not bear possessions and is discarded if not at a marsh.
 */
public class Card2_066 extends AbstractMinion {
    public Card2_066() {
        super(2, 7, 2, 4, Race.CREATURE, Culture.MORIA, "Huge Tentacle");
        addKeyword(Keyword.TENTACLE);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("Watcher in the Water")));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.POSSESSION), Filters.attachedTo(Filters.sameCard(self)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, Filters.and(Filters.type(CardType.POSSESSION), Filters.attachedTo(Filters.sameCard(self)))));
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
