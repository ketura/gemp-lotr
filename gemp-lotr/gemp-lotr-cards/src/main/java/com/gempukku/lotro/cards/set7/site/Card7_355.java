package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 6
 * Type: Site
 * Site: 7K
 * Game Text: River. Shadow: Spot your minion and 6 companions to make the move limit for this turn -1.
 */
public class Card7_355 extends AbstractSite {
    public Card7_355() {
        super("Ruined Capitol", SitesBlock.KING, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canSpot(game, Filters.owner(playerId), CardType.MINION)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, -1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
