package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * •Elrond, Peredhil
 * Elven	Ally • Elf • Rivendell
 * 8	4
 * At the start of each of your turns, heal every Rivendell ally.
 * Regroup: Exert Elrond twice to reveal the top card of your draw deck. If it is an [Elven] card,
 * heal a companion (or an Elf companion twice).
 */
public class Card20_080 extends AbstractAlly {
    public Card20_080() {
        super(4, null, 0, 8, 4, Race.ELF, Culture.ELVEN, "Elrond", "Peredhil", true);
        addKeyword(Keyword.RIVENDELL);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            RequiredTriggerAction action =new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, CardType.ALLY, Keyword.RIVENDELL));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfExert(self, 2, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (PhysicalCard revealedCard : revealedCards) {
                                if (Filters.and(Culture.ELVEN).accepts(game.getGameState(), game.getModifiersQuerying(), revealedCard)) {
                                    action.appendEffect(
                                            new ChooseActiveCardEffect(self, playerId, "Choose companion to heal", CardType.COMPANION, Filters.canHeal) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard companion) {
                                                    action.appendEffect(
                                                            new HealCharactersEffect(self, companion));
                                                    if (Filters.and(Race.ELF).accepts(game.getGameState(), game.getModifiersQuerying(), companion))
                                                        action.appendEffect(
                                                                new HealCharactersEffect(self, companion));
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
