package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

import java.util.Arrays;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Spell. Maneuver: Spot Gandalf to make a companion defender +1 until the regroup phase. Any Shadow player
 * may remove (3) to prevent this.
 */
public class Card2_028 extends AbstractEvent {
    public Card2_028() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Wielder of the Flame", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new ChooseActiveCardEffect(playerId, "Choose companion", Filters.type(CardType.COMPANION)) {
                            @Override
                            protected void cardSelected(PhysicalCard card) {
                                action.appendEffect(
                                        new AddUntilStartOfPhaseModifierEffect(
                                                new KeywordModifier(self, Filters.sameCard(card), Keyword.DEFENDER), Phase.REGROUP));
                            }
                        },
                        Arrays.asList(GameUtils.getOpponents(game, playerId)),
                        new RemoveTwilightEffect(3)));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
