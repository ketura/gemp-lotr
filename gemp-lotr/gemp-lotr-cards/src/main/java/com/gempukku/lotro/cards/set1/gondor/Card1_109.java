package com.gempukku.lotro.cards.set1.gondor;

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
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert Aragorn to spot an ally. Until the regroup phase, that ally is strength +2 and
 * participates in archery fire and skirmishes.
 */
public class Card1_109 extends AbstractEvent {
    public Card1_109() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "One Whom Men Would Follow", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.aragorn);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.aragorn) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ChooseActiveCardEffect(self, playerId, "Choose an ally", CardType.ALLY) {
                                    @Override
                                    protected void cardSelected(LotroGame game, PhysicalCard ally) {
                                        action.appendEffect(
                                                new AddUntilStartOfPhaseModifierEffect(
                                                        new StrengthModifier(self, Filters.sameCard(ally), 2)
                                                        , Phase.REGROUP));
                                        action.appendEffect(
                                                new AddUntilStartOfPhaseModifierEffect(
                                                        new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, Filters.sameCard(ally))
                                                        , Phase.REGROUP));
                                    }
                                }
                        );
                    }
                }
        );
        return action;
    }
}
