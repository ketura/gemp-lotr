package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Shadow: Exert a [ISENGARD] minion to discard all conditions.
 */
public class Card1_136 extends AbstractLotroCardBlueprint {
    public Card1_136() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Saruman's Power", "1_136");
        addKeyword(Keyword.SPELL);
        addKeyword(Keyword.SHADOW);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert())) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an ISENGARD minion", Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert()) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard isengardMinion) {
                            action.addCost(new ExertCharacterEffect(isengardMinion));
                        }
                    });
            action.addEffect(
                    new DiscardCardsFromPlayEffect(Filters.type(CardType.CONDITION)));

            return Collections.singletonList(action);
        }

        return null;
    }
}
