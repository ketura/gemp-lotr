package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.RevealCardFromTopOfDeckResult;

import java.util.*;

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
        super(2, SitesBlock.SECOND_ED, 0, 4, 3, Race.ELF, Culture.ELVEN, "Golradir", "Homely House Advisor", true);
        addKeyword(Keyword.RIVENDELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.revealedCardsFromTopOfDeck(effectResult, playerId)
                && PlayConditions.canSelfExert(self, game)) {
            RevealCardFromTopOfDeckResult revealedResult = (RevealCardFromTopOfDeckResult) effectResult;
            final PhysicalCard revealedCard = revealedResult.getRevealedCard();
            if (Filters.and(Culture.ELVEN).accepts(game, revealedCard)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
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
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
