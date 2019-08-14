package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Fiery Terrace
 * 8	8
 * Battleground.
 * Shadow: Play the Witch-king from your hand or draw deck; his twilight cost is -4.
 */
public class Card20_460 extends AbstractSite {
    public Card20_460() {
        super("Fiery Terrace", SitesBlock.SECOND_ED, 8, 8, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -4, Filters.witchKing) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play The Witch-king from your hand";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -4, Filters.witchKing) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play The Witch-king from your draw deck";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
