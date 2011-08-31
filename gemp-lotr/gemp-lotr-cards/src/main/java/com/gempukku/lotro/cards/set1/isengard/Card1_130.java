package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
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
public class Card1_130 extends AbstractLotroCardBlueprint {
    public Card1_130() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "No Ordinary Storm", "1_130");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.keyword(Keyword.WEATHER), Filters.type(CardType.CONDITION), Filters.owner(self.getOwner())))) {
            final CostToEffectAction action = new CostToEffectAction(self, null, "Exert a GANDALF companion or GANDALF ally");
            action.addEffect(
                    new ChooseActiveCardEffect(self.getOwner(), "Choose GANDALF companion or GANDALF ally", Filters.culture(Culture.GANDALF), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY))) {
                        @Override
                        protected void cardSelected(PhysicalCard gandalfCharacter) {
                            action.addEffect(new ExertCharacterEffect(gandalfCharacter));
                        }
                    }
            );

            return Collections.singletonList(action);
        }
        return null;
    }
}
