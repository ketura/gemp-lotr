package com.gempukku.lotro.cards.set12.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Fellowship
 * Game Text: Spell. Choose one: Spot a [GANDALF] Wizard to choose an opponent who must discard one of his or her
 * conditions from play; or spot a [GANDALF] Wizard at a battleground site to discard a condition from play.
 */
public class Card12_029 extends AbstractEvent {
    public Card12_029() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Introspection", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Make opponent discard condition";
                    }

                    @Override
                    protected void opponentChosen(String opponentId) {
                        action.appendEffect(
                                new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, 1, 1, Filters.owner(opponentId), CardType.CONDITION));
                    }
                });
        if (PlayConditions.location(game, Keyword.BATTLEGROUND))
            possibleEffects.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
