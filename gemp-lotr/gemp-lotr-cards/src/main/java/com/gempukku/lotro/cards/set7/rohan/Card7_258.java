package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make a mounted [ROHAN] Man strength +1 for each mounted companion. If that Man is skirmishing a [RAIDER]
 * minion, also make him or her strength +2 and damage +2.
 */
public class Card7_258 extends AbstractEvent {
    public Card7_258() {
        super(Side.FREE_PEOPLE, 2, Culture.ROHAN, "White Hot Fury", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose mounted ROHAN Man", Culture.ROHAN, Race.MAN, Filters.mounted) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, null, new CountActiveEvaluator(CardType.COMPANION, Filters.mounted)), Phase.SKIRMISH));
                        boolean skirmishingRaider = Filters.inSkirmishAgainst(Culture.RAIDER, CardType.MINION).accepts(game.getGameState(), game.getModifiersQuerying(), card);
                        if (skirmishingRaider) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DAMAGE, 2), Phase.SKIRMISH));
                        }
                    }
                });
        return action;
    }
}
