package com.gempukku.lotro.cards.set32.site;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: River. Sanctuary. Fellowship: Exert an [ESGAROTH] ally to play a weapon from your discard pile
 * (limit once per turn).
 */
public class Card32_053 extends AbstractSite {
    public Card32_053() {
        super("Town Hall", SitesBlock.HOBBIT, 6, 3, Direction.RIGHT);
        addKeyword(Keyword.RIVER);

    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Culture.ESGAROTH, CardType.ALLY)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ESGAROTH, CardType.ALLY));
            action.appendEffect(
                    new CheckPhaseLimitEffect(action, self, 1, Phase.FELLOWSHIP,
                            new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.weapon)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
