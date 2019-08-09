package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Twilight Cost: 6
 * Type: Site
 * Site: 5
 * Game Text: Forest. At the start of the Shadow phase, you may discard Gandalf to play a minion from
 * your discard pile.
 */
public class Card31_051 extends AbstractSite {
    public Card31_051() {
        super("Old Forest Road", SitesBlock.HOBBIT, 5, 6, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
         if (TriggerConditions.startOfPhase(game, effectResult, Phase.SHADOW)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.gandalf)
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.gandalf));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
