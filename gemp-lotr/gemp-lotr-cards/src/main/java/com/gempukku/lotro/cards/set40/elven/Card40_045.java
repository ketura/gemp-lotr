package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Galadriel, White Lady of Lorien
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Ally - Elf - Lothlorien
 * Strength: 3
 * Vitality: 3
 * Card Number: 1R45
 * Game Text: At the start of each of your turns, heal each Lothlorien ally.
 * Skirmish: Exert Galadriel to reveal the top card of your draw deck. If it is an [ELVEN] card, you may discard it
 * to make a minion skirmishing an Elf strength -2.
 */
public class Card40_045 extends AbstractAlly {
    public Card40_045() {
        super(3, SitesBlock.SECOND_ED, 0, 3, 3, Race.ELF, Culture.ELVEN, "Galadriel", "White Lady of Lorien", true);
        addKeyword(Keyword.LOTHLORIEN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, CardType.ALLY, Keyword.LOTHLORIEN));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            if (revealedCards.size() > 0) {
                                if (Filters.and(Culture.ELVEN).accepts(game,
                                        revealedCards.get(0))) {
                                    action.appendCost(
                                            new OptionalEffect(action, playerId,
                                                    new UnrespondableEffect() {
                                                        @Override
                                                        protected void doPlayEffect(LotroGame game) {
                                                            action.appendCost(
                                                                    new DiscardTopCardFromDeckEffect(self, playerId, false));
                                                            action.appendEffect(
                                                                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -2,
                                                                            CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)));
                                                        }

                                                        @Override
                                                        public String getText(LotroGame game) {
                                                            return "Discard the revealed card to make a minion skirmishing an Elf strength -2";
                                                        }
                                                    }));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
