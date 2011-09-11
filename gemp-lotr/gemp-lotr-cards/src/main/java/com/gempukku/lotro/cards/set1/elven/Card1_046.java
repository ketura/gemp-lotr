package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
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
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, exert an Elf ally. Plays to your support area. When the fellowship moves from a river during the
 * fellowship phase, the move limit for this turn is +1.
 */
public class Card1_046 extends AbstractPermanent {
    public Card1_046() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.FREE_SUPPORT, "Gift of Boats", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF), Filters.type(CardType.ALLY), Filters.canExert());
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayPermanentAction action = getPlayCardAction(playerId, game, self, twilightModifier);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose an Elf ally to exert", true, Filters.race(Race.ELF), Filters.type(CardType.ALLY), Filters.canExert()));
        return action;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_FROM
                && game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Move limit for this turn is +1");
            action.addEffect(new AddUntilEndOfTurnModifierEffect(new MoveLimitModifier(self, 1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
