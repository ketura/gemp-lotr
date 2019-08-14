package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * •Galadriel, White Lady of Lorien
 * Elven	Ally • Elf • Lothlorien
 * 3	3
 * At the start of each of your turns, heal every Lothlorien ally.
 * Skirmish: Exert Galadriel to reveal the top card of your draw deck. Make a minion skirmishing an Elf strength - X
 * where X is the revealed card's twilight cost.
 */
public class Card20_084 extends AbstractAlly {
    public Card20_084() {
        super(3, null, 0, 3, 3, Race.ELF, Culture.ELVEN, "Galadriel", "White Lady of Lorien", true);
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
            final ActivateCardAction action =new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (PhysicalCard revealedCard : revealedCards) {
                                int twilightCost = revealedCard.getBlueprint().getTwilightCost();
                                action.appendEffect(
                                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -twilightCost, CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
