package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: River. When the fellowship moves to Silverlode Banks without a ranger, every companion must exert.
 */
public class Card1_355 extends AbstractSite {
    public Card1_355() {
        super("Silverlode Banks", 7, 6, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self
                && !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RANGER))) {
            List<PhysicalCard> companions = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION));
            List<Action> actions = new LinkedList<Action>();
            for (PhysicalCard companion : companions) {
                CostToEffectAction action = new CostToEffectAction(self, null, "Exert companion");
                action.addEffect(new ExertCharacterEffect(companion));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
