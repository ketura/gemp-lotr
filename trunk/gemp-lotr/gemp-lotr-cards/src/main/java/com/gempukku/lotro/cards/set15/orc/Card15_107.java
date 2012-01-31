package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot 3 Free Peoples cultures or discard 2 [ORC] conditions from play.
 */
public class Card15_107 extends AbstractMinion {
    public Card15_107() {
        super(2, 12, 3, 4, Race.ORC, Culture.ORC, "Desolation Orc");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && (new CountCulturesEvaluator(Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null) >= 3
                || PlayConditions.canDiscardFromPlay(self, game, 2, Culture.ORC, CardType.CONDITION));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        if (new CountCulturesEvaluator(Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null) >= 3)
            possibleCosts.add(
                    new UnrespondableEffect() {
                        @Override
                        public String getText(LotroGame game) {
                            return "Spot 3 Free Peoples cultures";
                        }

                        @Override
                        protected void doPlayEffect(LotroGame game) {
                        }
                    });
        possibleCosts.add(
                new ChooseAndDiscardCardsFromPlayEffect(playCardAction, playerId, 2, 2, Culture.ORC, CardType.CONDITION));
        playCardAction.appendCost(
                new ChoiceEffect(playCardAction, playerId, possibleCosts));
        return playCardAction;
    }
}
