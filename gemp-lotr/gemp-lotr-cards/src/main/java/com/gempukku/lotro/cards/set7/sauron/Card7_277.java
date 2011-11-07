package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. Shadow: Discard 2 cards from hand to play a [SAURON] Orc stacked on a site you control (discard
 * 1 card from hand instead if that Orc is a besieger).
 */
public class Card7_277 extends AbstractMinion {
    public Card7_277() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Sapper");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any) && PlayConditions.canPlayFromStacked(playerId, game, Filters.siteControlled(playerId), Culture.SAURON, Race.ORC)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play stacked SAURON Orc");
                action.appendCost(
                        new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
                action.appendEffect(
                        new ChooseAndPlayCardFromStackedEffect(playerId, Filters.siteControlled(playerId), Culture.SAURON, Race.ORC));
                actions.add(action);
            }
            if (PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any) && PlayConditions.canPlayFromStacked(playerId, game, Filters.siteControlled(playerId), Culture.SAURON, Race.ORC, Keyword.BESIEGER)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Play stacked besieger SAURON Orc");
                action.appendCost(
                        new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1));
                action.appendEffect(
                        new ChooseAndPlayCardFromStackedEffect(playerId, Filters.siteControlled(playerId), Culture.SAURON, Race.ORC, Keyword.BESIEGER));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
