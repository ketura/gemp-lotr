package com.gempukku.lotro.cards.set8.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Skirmish
 * Game Text: If Gandalf is not assigned to a skirmish, spot Shadowfax or exert Gandalf to have Gandalf replace
 * an unbound companion in a skirmish.
 */
public class Card8_017 extends AbstractEvent {
    public Card8_017() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Mighty Steed", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Filters.gandalf, Filters.notAssignedToSkirmish)
                && (PlayConditions.canSpot(game, Filters.name("Shadowfax")) || PlayConditions.canExert(self, game, Filters.gandalf));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new SpotEffect(1, Filters.name("Shadowfax")) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Spot Shadowfax";
                    }
                });
        possibleCosts.add(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert Gandalf";
                    }
                });

        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose Gandalf", Filters.gandalf) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new ReplaceInSkirmishEffect(card, Filters.unboundCompanion));
                    }
                });
        return action;
    }
}
