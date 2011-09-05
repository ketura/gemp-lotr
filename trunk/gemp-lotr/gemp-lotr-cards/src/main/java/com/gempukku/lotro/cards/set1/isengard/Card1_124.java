package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Weather. Maneuver: Exert a [ISENGARD] minion to make the opponent choose to either exert the
 * Ring-bearer or add a burden.
 */
public class Card1_124 extends AbstractLotroCardBlueprint {
    public Card1_124() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Cruel Caradhras");
        addKeyword(Keyword.SPELL);
        addKeyword(Keyword.WEATHER);
        addKeyword(Keyword.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert())) {
            final PlayEventAction action = new PlayEventAction(self);
            String fpPlayer = game.getGameState().getCurrentPlayerId();

            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an ISENGARD minion", Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard isengardMinion) {
                            action.addCost(new ExertCharacterEffect(isengardMinion));
                        }
                    }
            );

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(new ExertCharacterEffect(game.getGameState().getRingBearer(fpPlayer)));
            possibleEffects.add(new AddBurdenEffect(fpPlayer));

            action.addEffect(
                    new ChoiceEffect(action, fpPlayer, possibleEffects, false));

            return Collections.singletonList(action);
        }

        return null;
    }
}
