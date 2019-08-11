package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Hamfast Gamgee, The Gaffer
 * Ally • Hobbit • Shire
 * 2	2
 * Fellowship: Play a Hobbit ally to heal a Hobbit companion.
 * http://lotrtcg.org/coreset/shire/hamfastgamgeetg(r2).jpg
 */
public class Card20_390 extends AbstractAlly {
    public Card20_390() {
        super(1, null, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Hamfast Gamgee", "The Gaffer", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    protected List<ActivateCardAction> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, Race.HOBBIT, CardType.ALLY)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Race.HOBBIT, CardType.ALLY));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Race.HOBBIT, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
