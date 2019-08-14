package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Elrond, Peredhil
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Ally - Elf - Rivendell
 * Strength: 8
 * Vitality: 4
 * Card Number: 1R41
 * Game Text: At the start of each of your turns, heal each Rivendell ally.
 * Regroup: Exert Elrond twice to reveal the top card of your draw deck. If it is an [ELVEN] card, you may discard it
 * to heal a companion. If it is a Shadow card, you may discard it to discard a Shadow condition.
 */
public class Card40_041 extends AbstractAlly {
    public Card40_041() {
        super(4, SitesBlock.SECOND_ED, 0, 8, 4, Race.ELF, Culture.ELVEN, "Elrond", "Peredhil", true);
        addKeyword(Keyword.RIVENDELL);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, CardType.ALLY, Keyword.RIVENDELL) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Heal each Rivendell ally";
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfExert(self, 2, game)) {
            String[] allPlayers = game.getGameState().getPlayerOrder().getAllPlayers().toArray(new String[0]);

            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            if (revealedCards.size() > 0) {
                                PhysicalCard firstCard = revealedCards.get(0);
                                if (Filters.and(Side.FREE_PEOPLE).accepts(game,
                                        firstCard)) {
                                    action.appendCost(
                                            new OptionalEffect(action, playerId,
                                                    new UnrespondableEffect() {
                                                        @Override
                                                        protected void doPlayEffect(LotroGame game) {
                                                            action.appendCost(
                                                                    new DiscardTopCardFromDeckEffect(self, playerId, false));
                                                            action.appendEffect(
                                                                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
                                                        }

                                                        @Override
                                                        public String getText(LotroGame game) {
                                                            return "Discard the revealed card to heal a companion";
                                                        }
                                                    }));
                                } else {
                                    action.appendEffect(
                                            new UnrespondableEffect() {
                                                @Override
                                                protected void doPlayEffect(LotroGame game) {
                                                    action.appendCost(
                                                            new DiscardTopCardFromDeckEffect(self, playerId, false));
                                                    action.appendEffect(
                                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, CardType.CONDITION));
                                                }

                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Discard the revealed card to discard a shadow condition";
                                                }
                                            });
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
