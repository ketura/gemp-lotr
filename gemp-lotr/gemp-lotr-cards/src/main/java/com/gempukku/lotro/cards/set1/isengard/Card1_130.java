package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each time you play a weather condition, exert a [GANDALF] companion or [GANDALF] ally.
 */
public class Card1_130 extends AbstractPermanent {
    public Card1_130() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, Zone.SHADOW_SUPPORT, "No Ordinary Storm");
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.keyword(Keyword.WEATHER), Filters.type(CardType.CONDITION), Filters.owner(self.getOwner())))) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Exert a GANDALF companion or GANDALF ally");
            action.addEffect(
                    new ChooseAndExertCharacterEffect(action, self.getOwner(), "Choose GANDALF companion or GANDALF ally", false, Filters.culture(Culture.GANDALF), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY))));

            return Collections.singletonList(action);
        }
        return null;
    }
}
