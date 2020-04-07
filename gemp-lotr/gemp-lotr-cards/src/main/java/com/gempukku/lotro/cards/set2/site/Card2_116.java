package com.gempukku.lotro.cards.set2.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Type: Site
 * Site: 1
 * Game Text: Forest. Fellowship: Exert a Hobbit to play your site 2 (replacing opponent's site if necessary).
 */
public class Card2_116 extends AbstractSite {
    public Card2_116() {
        super("Hobbiton Woods", SitesBlock.FELLOWSHIP, 1, 0, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Race.HOBBIT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT));
            action.appendEffect(
                    new PlaySiteEffect(action, playerId, SitesBlock.FELLOWSHIP, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
