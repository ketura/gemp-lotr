package com.gempukku.lotro.cards.set8.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.PlayerCantUsePhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.RuleUtils;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event • Archery
 * Game Text: Spot an Elf companion and make the fellowship archery total -X (to a minimum of 0) to heal X unbound
 * companions. You cannot use archery special abilities.
 */
public class Card8_009 extends AbstractEvent {
    public Card8_009() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "A Grey Ship", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        int archeryTotal = RuleUtils.calculateFellowshipArcheryTotal(game);
        action.appendCost(
                new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "Choose X (reduce fellowship archery total)", 0, archeryTotal) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int reduce = getValidatedResult(result);
                                action.appendCost(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -reduce)));
                                action.appendEffect(
                                        new ChooseAndHealCharactersEffect(action, playerId, reduce, reduce, Filters.unboundCompanion));
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new PlayerCantUsePhaseSpecialAbilitiesModifier(self, playerId, Phase.ARCHERY)));
                            }
                        }));
        return action;
    }
}
