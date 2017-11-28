package com.gempukku.lotro.cards.set30.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Twilight Cost: 6
 * Type: Site
 * Site: 5
 * Game Text: Forest. When the fellowship moves to Mirkwood, discard Gandalf.
 */
public class Card30_053 extends AbstractSite {
    public Card30_053() {
        super("Mirkwood", Block.HOBBIT, 5, 6, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
				new DiscardCardsFromPlayEffect(self.getOwner(), self, Filters.gandalf));
            return Collections.singletonList(action);
        }
        return null;
    }
}