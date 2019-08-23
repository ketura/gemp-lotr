package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Steward's Chamber
 * 6	3
 * Sanctuary.
 * Fellowship: Exert a companion and add (1) to heal a companion of another culture.
 */
public class Card20_449 extends AbstractSite {
    public Card20_449() {
        super("Steward's Chamber", SitesBlock.SECOND_ED, 6, 3, null);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, CardType.COMPANION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION, Filters.not(character.getBlueprint().getCulture())));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
