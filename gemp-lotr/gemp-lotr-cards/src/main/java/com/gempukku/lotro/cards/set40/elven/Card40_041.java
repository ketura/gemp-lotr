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
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

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
 * Game Text: At the start of each of your turns, heal each Rivendell ally or heal Elrond twice.
 * Regroup: Exert Elrond twice to reveal the top card of any draw deck. If it is a Free Peoples card, you may heal
 * a companion. If it is a Shadow card, you may discard a condition.
 */
public class Card40_041 extends AbstractAlly{
    public Card40_041() {
        super(4, SitesBlock.SECOND_ED, 0, 8, 4, Race.ELF, Culture.ELVEN, "Elrond", "Peredhil", true);
        addKeyword(Keyword.RIVENDELL);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new ArrayList<Effect>(2);
            possibleEffects.add(
                    new HealCharactersEffect(self, CardType.ALLY, Keyword.RIVENDELL) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Heal each Rivendell ally";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndHealCharactersEffect(action, self.getOwner(), 1, 1, 2, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Heal Elrond twice";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<ActivateCardAction> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfExert(self, 2, game)) {
            String[] allPlayers = game.getGameState().getPlayerOrder().getAllPlayers().toArray(new String[0]);

            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new PlayoutDecisionEffect(playerId,
                            new MultipleChoiceAwaitingDecision(1, "Choose a player to reveal top card of deck of", allPlayers) {
                                @Override
                                protected void validDecisionMade(int index, String player) {
                                    action.appendEffect(
                                            new RevealTopCardsOfDrawDeckEffect(self, player, 1) {
                                                @Override
                                                protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                                                    if (revealedCards.size()>0) {
                                                        PhysicalCard firstCard = revealedCards.get(0);
                                                        if (Filters.and(Side.FREE_PEOPLE).accepts(game,
                                                                firstCard)) {
                                                            action.appendEffect(
                                                                    new OptionalEffect(action, playerId,
                                                                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, CardType.COMPANION)) {
                                                                        @Override
                                                                        public String getText(LotroGame game) {
                                                                            return "Heal a companion";
                                                                        }
                                                                    });
                                                        } else {
                                                            action.appendEffect(
                                                                    new OptionalEffect(action, playerId,
                                                                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1,1, CardType.CONDITION) {
                                                                        @Override
                                                                        public String getText(LotroGame game) {
                                                                            return "Discard a condition";
                                                                        }
                                                                    }));
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
