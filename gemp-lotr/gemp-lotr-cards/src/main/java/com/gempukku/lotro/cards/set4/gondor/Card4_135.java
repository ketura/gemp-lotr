package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a [GONDOR] Man strength +2 (or +3 and damage +1 if skirmishing a roaming minion).
 */
public class Card4_135 extends AbstractOldEvent {
    public Card4_135() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "War and Valor", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR Man", Culture.GONDOR, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        boolean isAgainstRoaming = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.roaminMinion, Filters.inSkirmishAgainst(Filters.sameCard(card))) > 0;
                        int bonus = isAgainstRoaming ? 3 : 2;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus), Phase.SKIRMISH));
                        if (isAgainstRoaming)
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(card), Keyword.DAMAGE), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
