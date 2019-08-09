package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: River. When the fellowship moves to Anduin Confluence, discard every ally.
 */
public class Card1_353 extends AbstractSite {
    public Card1_353() {
        super("Anduin Confluence", SitesBlock.FELLOWSHIP, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self, CardType.ALLY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
