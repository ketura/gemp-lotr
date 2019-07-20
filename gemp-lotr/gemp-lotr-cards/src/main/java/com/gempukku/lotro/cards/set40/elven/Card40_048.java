package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RevealCardFromTopOfDeckResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Title: *Golradir, Homely House Advisor
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally - Elf - Rivendell
 * Strength: 4
 * Vitality: 3
 * Card Number: 1C48
 * Game Text: To play, spot an Elf.
 * Each time you reveal an [ELVEN] card from the top of your draw deck, you may exert Golradir to make an Orc strength -2 until the regroup phase.
 */
public class Card40_048 extends AbstractAlly {
    public Card40_048() {
        super(2, Block.SECOND_ED, 0, 4, 3, Race.ELF, Culture.ELVEN, "Golradir", "Homely House Advisor", true);
        addKeyword(Keyword.RIVENDELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.revealedCardsFromTopOfDeck(effectResult, playerId)
                && PlayConditions.canSelfExert(self, game)) {
            RevealCardFromTopOfDeckResult revealedResult = (RevealCardFromTopOfDeckResult) effectResult;
            Collection<PhysicalCard> revealedCards = revealedResult.getRevealedCards();
            int countOfElven = getElvenCount(revealedCards);
            if (countOfElven > 0) {
                List<OptionalTriggerAction> actions = new ArrayList<OptionalTriggerAction>(countOfElven);
                for (int i = 0; i < countOfElven; i++) {
                    final OptionalTriggerAction action = new OptionalTriggerAction("golradir-" + i, self);
                    action.appendCost(
                            new SelfExertEffect(action, self));
                    action.appendEffect(
                            new ChooseActiveCardEffect(self, playerId, "Choose an Orc", Race.ORC) {
                                @Override
                                protected void cardSelected(LotroGame game, PhysicalCard card) {
                                    action.appendEffect(
                                            new AddUntilStartOfPhaseModifierEffect(
                                                    new StrengthModifier(self, card, -2), Phase.REGROUP));
                                }
                            });
                    actions.add(action);
                }
                return actions;
            }
        }
        return null;
    }

    private int getElvenCount(Collection<PhysicalCard> revealedCards) {
        int countOfElven = 0;
        for (PhysicalCard revealedCard : revealedCards) {
            if (revealedCard.getBlueprint().getCulture() == Culture.ELVEN)
                countOfElven++;
        }
        return countOfElven;
    }
}
