package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Farmer Maggot, Chaser of Rascals
 * Shire	Ally • Hobbit • Shire
 * 2	3
 * Fellowship: Exert Farmer Maggot to heal Merry or Pippin.
 */
public class Card20_385 extends AbstractAlly {
    public Card20_385() {
        super(1, null, 0, 2, 3, Race.HOBBIT, Culture.SHIRE, "Farmer Maggot", "Chaser of Rascals", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.or(Filters.name("Merry"), Filters.name("Pippin"))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
