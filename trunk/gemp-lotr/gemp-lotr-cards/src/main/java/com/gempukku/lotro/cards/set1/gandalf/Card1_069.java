package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Man
 * Strength: 3
 * Vitality: 3
 * Site: 1
 * Game Text: To play, spot Gandalf. Maneuver: Exert Albert Dreary to discard a [ISENGARD] or [MORIA] condition.
 */
public class Card1_069 extends AbstractAlly {
    public Card1_069() {
        super(1, 1, 3, 3, Race.MAN, Culture.GANDALF, "Albert Dreary", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Exert Albert Dreary to discard a ISENGARD or MORIA condition.");
            action.addCost(new ExertCharacterEffect(playerId, self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose ISENGARD or MORIA condition", Filters.or(Filters.culture(Culture.ISENGARD), Filters.culture(Culture.MORIA)), Filters.type(CardType.CONDITION)) {
                        @Override
                        protected void cardSelected(PhysicalCard condition) {
                            action.addEffect(new DiscardCardFromPlayEffect(self, condition));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
