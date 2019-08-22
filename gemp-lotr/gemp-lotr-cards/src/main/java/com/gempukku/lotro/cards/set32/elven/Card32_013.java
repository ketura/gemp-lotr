package com.gempukku.lotro.cards.set32.elven;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Ally • Home 3 • Elf
 * Strength: 3
 * Vitality: 3
 * Site: 3
 * Game Text: At the start of each of your turns, you may heal Gandalf.
Skirmish: Exert Galadriel and discard a Shadow card from hand to make a minion strength -2 (or -3 if a Wise character is skirmishing that minion).
 */
public class Card32_013 extends AbstractAlly {
    public Card32_013() {
        super(3, SitesBlock.HOBBIT, 3, 3, 3, Race.ELF, Culture.ELVEN, "Galadriel", "Elven Lady", true);
        addKeyword(Keyword.WISE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.gandalf));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Side.SHADOW)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Side.SHADOW));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
                @Override
                public void cardSelected(LotroGame game, PhysicalCard minion) {
                    final boolean againstWise = Filters.inSkirmishAgainst(Filters.and(Keyword.WISE)).accepts(game, minion);
                    int bonus = againstWise ? -3 : -2;
                    action.insertEffect(
                            new AddUntilEndOfPhaseModifierEffect(
new StrengthModifier(self, Filters.sameCard(minion), bonus)));
                }
            });
            return Collections.singletonList(action);
        }
        return null;
    }
}
