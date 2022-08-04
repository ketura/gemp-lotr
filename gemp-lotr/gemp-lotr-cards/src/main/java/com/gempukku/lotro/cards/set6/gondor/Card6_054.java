package com.gempukku.lotro.cards.set6.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 1, 2, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self, true);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Keyword.RANGER));
        List<Effect> possibleEffects = new LinkedList<>();
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
