package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 0
 * War and Valor
 * Gondor	Event • Skirmish
 * Make a [Gondor] Man strength +2 (or strength +3 and damage +1 if skirmishing a roaming minion).
 */
public class Card20_207 extends AbstractEvent {
    public Card20_207() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "War and Valor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR ranger", Culture.GONDOR, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard ranger) {
                        boolean matches = Filters.inSkirmishAgainst(CardType.MINION, Keyword.ROAMING).accepts(
                                game.getGameState(), game.getModifiersQuerying(), ranger);
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, ranger, matches?3:2)));
                        if (matches)
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, ranger, Keyword.DAMAGE, 1)));
                    }
                });
        return action;
    }
}
