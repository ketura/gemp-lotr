package com.gempukku.lotro.cards.set6.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Wraith
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: Twilight. Skirmish: Exert this minion to make a [SAURON] or twilight minion strength +1 for each twilight
 * minion you can spot.
 */
public class Card6_099 extends AbstractMinion {
    public Card6_099() {
        super(3, 7, 2, 4, Race.WRAITH, Culture.SAURON, "Corpse Lights");
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new CountSpottableEvaluator(CardType.MINION, Keyword.TWILIGHT),
                            CardType.MINION, Filters.or(Culture.SAURON, Keyword.TWILIGHT)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
