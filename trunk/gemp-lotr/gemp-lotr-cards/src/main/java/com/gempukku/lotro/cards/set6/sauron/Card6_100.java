package com.gempukku.lotro.cards.set6.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Minion • Wraith
 * Strength: 5
 * Vitality: 1
 * Site: 4
 * Game Text: Twilight. Damage +1. To play this minion, remove a burden or spot a twilight minion.
 */
public class Card6_100 extends AbstractMinion {
    public Card6_100() {
        super(1, 5, 1, 4, Race.WRAITH, Culture.SAURON, "Dead Ones");
        addKeyword(Keyword.TWILIGHT);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && (game.getGameState().getBurdens() >= 1
                || PlayConditions.canSpot(game, CardType.MINION, Keyword.TWILIGHT));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new RemoveBurdenEffect(self) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Remove a burden";
                    }
                });
        possibleCosts.add(
                new SpotEffect(1, CardType.MINION, Keyword.TWILIGHT) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Spot a twilight minion";
                    }
                });
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));
        return action;
    }
}
