package com.gempukku.lotro.cards.set31.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Skillful Negociator [Shire]
 Event • Maneuver
 Twilight Cost 1
 'Spot an ally and exert Bilbo to allow that ally to participate in archery fire and skirmishes until the regroup phase.'
 */
public class Card31_043 extends AbstractEvent {
    public Card31_043() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Skillful Negociator", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, CardType.ALLY)
                && PlayConditions.canExert(self, game, Filters.name("Bilbo"));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Bilbo")));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an ally", CardType.ALLY) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, card), Phase.REGROUP));
                    }
                });
        return action;
    }
}
