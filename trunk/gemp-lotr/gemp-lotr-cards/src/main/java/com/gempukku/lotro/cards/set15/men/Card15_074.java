package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Fierce. Skirmish: Spot a site you control and remove (2) to make a [MEN] Man strength +2.
 * Skirmish: Spot 2 sites you control and remove (2) to make a [MEN] Man strength +3.
 * Skirmish: Spot 3 sites you control and remove (2) to make a [MEN] Man strength +4.
 */
public class Card15_074 extends AbstractMinion {
    public Card15_074() {
        super(5, 11, 3, 4, Race.MAN, Culture.MEN, "Chieftain of Dunland", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 2)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();

            if (PlayConditions.canSpot(game, Filters.siteControlled(playerId))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make a MEN Man strength +2");
                action.appendCost(
                        new RemoveTwilightEffect(2));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(
                                action, self, playerId, 2, Culture.MEN, Race.MAN));
                actions.add(action);
            }
            if (PlayConditions.canSpot(game, 2, Filters.siteControlled(playerId))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make a MEN Man strength +3");
                action.appendCost(
                        new RemoveTwilightEffect(2));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(
                                action, self, playerId, 3, Culture.MEN, Race.MAN));
                actions.add(action);
            }
            if (PlayConditions.canSpot(game, 3, Filters.siteControlled(playerId))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make a MEN Man strength +4");
                action.appendCost(
                        new RemoveTwilightEffect(2));
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(
                                action, self, playerId, 4, Culture.MEN, Race.MAN));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
