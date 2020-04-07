package com.gempukku.lotro.cards.set18.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Spell. Spot a [GANDALF] Wizard to make a minion lose all strength bonuses from possessions.
 */
public class Card18_019 extends AbstractEvent {
    public Card18_019() {
        super(Side.FREE_PEOPLE, 0, Culture.GANDALF, "Drawn to Full Height", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new CancelStrengthBonusTargetModifier(self, card, CardType.POSSESSION)));
                    }
                });
        return action;
    }
}
