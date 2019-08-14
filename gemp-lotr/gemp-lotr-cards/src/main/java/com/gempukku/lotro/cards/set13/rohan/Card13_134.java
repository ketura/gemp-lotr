package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make your [ROHAN] companion strength +2 (or exert Theoden to make that companion strength +5
 * and damage +1 instead.)
 */
public class Card13_134 extends AbstractEvent {
    public Card13_134() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Ride With Me", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        if (PlayConditions.canExert(self, game, Filters.name(Names.theoden))) {
            action.appendCost(
                    new PlayoutDecisionEffect(playerId,
                            new YesNoDecision("Do you want to exert Theoden?") {
                                @Override
                                protected void yes() {
                                    action.appendCost(
                                            new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name(Names.theoden)));
                                    action.appendEffect(
                                            new ChooseActiveCardEffect(self, playerId, "Choose your ROHAN companion", Filters.owner(playerId), Culture.ROHAN, CardType.COMPANION) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                    action.appendEffect(
                                                            new AddUntilEndOfPhaseModifierEffect(
                                                                    new StrengthModifier(self, card, 5)));
                                                    action.appendEffect(
                                                            new AddUntilEndOfPhaseModifierEffect(
                                                                    new KeywordModifier(self, card, Keyword.DAMAGE, 1)));
                                                }
                                            });
                                }

                                @Override
                                protected void no() {
                                    action.appendEffect(
                                            new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.owner(playerId), Culture.ROHAN, CardType.COMPANION));
                                }
                            }));
        } else {
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.owner(playerId), Culture.ROHAN, CardType.COMPANION));
        }
        return action;
    }
}
