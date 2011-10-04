package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collections;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot an [ISENGARD] minion to exhaust Aragorn. The Free Peoples player may add 2 burdens
 * to prevent this.
 */
public class Card3_050 extends AbstractEvent {
    public Card3_050() {
        super(Side.SHADOW, Culture.ISENGARD, "Can You Protect Me From Yourself?", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        PhysicalCard aragorn = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Aragorn"));
        if (aragorn != null)
            action.appendEffect(
                    new PreventableEffect(action,
                            new ExhaustCharacterEffect(playerId, action, aragorn),
                            Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                            new AddBurdenEffect(self, 2)));
        return action;
    }
}
