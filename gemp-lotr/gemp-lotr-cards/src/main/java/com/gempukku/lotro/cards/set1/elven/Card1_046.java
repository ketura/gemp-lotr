package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
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
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, exert an Elf ally. Plays to your support area. When the fellowship moves from a river during the
 * fellowship phase, the move limit for this turn is +1.
 */
public class Card1_046 extends AbstractLotroCardBlueprint {
    public Card1_046() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.ELVEN, "Gift of Boats", true);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.type(CardType.ALLY), Filters.canExert())) {
            final PlayPermanentAction action = new PlayPermanentAction(self, Zone.FREE_SUPPORT);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an Elf ally to exert", Filters.keyword(Keyword.ELF), Filters.type(CardType.ALLY), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard elfAlly) {
                            action.addCost(new ExertCharacterEffect(elfAlly));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_FROM) {
            CostToEffectAction action = new CostToEffectAction(self, null, "Move limit for this turn is +1");
            action.addEffect(new AddUntilEndOfTurnModifierEffect(new MoveLimitModifier(self, 1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
