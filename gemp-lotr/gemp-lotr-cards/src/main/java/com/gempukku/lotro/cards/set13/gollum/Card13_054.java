package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.SpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Spot Smeagol or exert Gollum to make Smeagol strength +2 and you may reinforce a [GOLLUM] token.
 */
public class Card13_054 extends AbstractEvent {
    public Card13_054() {
        super(Side.FREE_PEOPLE, 1, Culture.GOLLUM, "Out of All Knowledge", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return (PlayConditions.canSpot(game, Filters.smeagol) || PlayConditions.canExert(self, game, Filters.gollum));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gollum) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert Gollum";
                    }
                });
        possibleCosts.add(
                new SpotEffect(1, Filters.smeagol) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Spot Smeagol";
                    }
                });
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.smeagol));
        action.appendEffect(
                new OptionalEffect(action, playerId,
                        new ReinforceTokenEffect(self, playerId, Token.GOLLUM)));
        return action;
    }
}
