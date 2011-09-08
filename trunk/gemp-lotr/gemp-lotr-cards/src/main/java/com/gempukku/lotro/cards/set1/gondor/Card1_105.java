package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Maneuver: Exert a [GONDOR] character to wound a [SAURON] minion.
 */
public class Card1_105 extends AbstractLotroCardBlueprint {
    public Card1_105() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.GONDOR, "Foes of Mordor");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self))
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)), Filters.canExert())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Exert a GONDOR character to wound a SAURON minion.");
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose a GONDOR character", true, Filters.culture(Culture.GONDOR), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)), Filters.canExert()));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a SAURON minion", Filters.culture(Culture.SAURON), Filters.type(CardType.MINION)) {
                        @Override
                        protected void cardSelected(PhysicalCard sauronMinion) {
                            action.addEffect(new WoundCharacterEffect(sauronMinion));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
