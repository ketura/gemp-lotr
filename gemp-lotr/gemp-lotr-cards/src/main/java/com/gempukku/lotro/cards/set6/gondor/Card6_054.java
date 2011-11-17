package com.gempukku.lotro.cards.set6.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Regroup: Exert 2 rangers to discard a minion (or 2 [WRAITH] minions).
 */
public class Card6_054 extends AbstractEvent {
    public Card6_054() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Perilous Ventures", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, 1, 2, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self, true);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Keyword.RANGER));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard a minion";
                    }
                });
        possibleEffects.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, CardType.MINION, Culture.WRAITH) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard 2 WRAITH minions";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
