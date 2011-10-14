package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 5
 * Game Text: Underground. Shadow: Play The Balrog from your draw deck or hand; The Balrog's twilight cost is -6.
 */
public class Card1_349 extends AbstractSite {
    public Card1_349() {
        super("The Bridge of Khazad-dum", Block.FELLOWSHIP, 5, 6, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.SHADOW, self)) {
            ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.name("The Balrog"), Filters.playable(game, -6)).size() > 0) {
                // Play from hand
                possibleEffects.add(
                        new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Filters.name("The Balrog"), -6));
            }

            // Play from deck
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("Balrog"), -6));

            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));

            return Collections.singletonList(action);
        }
        return null;
    }
}
