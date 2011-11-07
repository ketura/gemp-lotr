package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
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
 * Game Text: Besieger. Regroup: Discard 2 cards from hand to stack a [SAURON] Orc on a site you control (or discard
 * 1 card from hand if that Orc is a besieger).
 */
public class Card7_273 extends AbstractMinion {
    public Card7_273() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Garrison");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Stack besieger SAURON Orc");
                action.appendCost(
                        new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.SAURON, Race.ORC, Keyword.BESIEGER));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose site you control", Filters.siteControlled(playerId)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new StackCardFromPlayEffect(self, card));
                            }
                        });
                actions.add(action);
            }
            if (PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Stack SAURON Orc");
                action.appendCost(
                        new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.SAURON, Race.ORC));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose site you control", Filters.siteControlled(playerId)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new StackCardFromPlayEffect(self, card));
                            }
                        });
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
