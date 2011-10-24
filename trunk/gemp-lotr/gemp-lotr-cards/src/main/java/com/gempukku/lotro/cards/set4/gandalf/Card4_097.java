package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.PreventAllWoundsActionProxy;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

import java.util.Arrays;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell.
 * Skirmish: Spot Gandalf to prevent all wounds to him. Any Shadow player may make you wound a minion to prevent this.
 */
public class Card4_097 extends AbstractOldEvent {
    public Card4_097() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Long I Fell", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new AddUntilEndOfPhaseActionProxyEffect(
                                new PreventAllWoundsActionProxy(self, Filters.name("Gandalf")), Phase.SKIRMISH) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Prevent all wounds to Gandalf";
                            }
                        },
                        Arrays.asList(GameUtils.getOpponents(game, playerId)),
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.type(CardType.MINION)) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Make fellowship player wound a minion";
                            }
                        }
                ));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
