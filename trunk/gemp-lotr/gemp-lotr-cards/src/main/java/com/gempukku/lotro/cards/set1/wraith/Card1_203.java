package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventWoundEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Nazgul is about to take a wound, prevent that wound.
 */
public class Card1_203 extends AbstractResponseEvent {
    public Card1_203() {
        super(Side.SHADOW, Culture.WRAITH, "All Blades Perish");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, final Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND) {
            final WoundCharacterEffect woundEffect = (WoundCharacterEffect) effect;
            if (Filters.filter(woundEffect.getCardsToBeWounded(game), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL)).size() > 0
                    && PlayConditions.canPayForShadowCard(game, self, 0)) {
                final PlayEventAction action = new PlayEventAction(self);
                action.addEffect(
                        new ChooseActiveCardEffect(playerId, "Choose a Nazgul", Filters.race(Race.NAZGUL), Filters.in(woundEffect.getCardsToBeWounded(game))) {
                            @Override
                            protected void cardSelected(PhysicalCard nazgul) {
                                action.addEffect(
                                        new PreventWoundEffect(woundEffect, nazgul));
                            }
                        }
                );
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
