package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Fortification. Plays to your support area. Archery: Spot 2 [ROHAN] Men to remove 2 tokens from a machine
 * or to make the minion archery total -3. Discard this condition.
 */
public class Card5_087 extends AbstractPermanent {
    public Card5_087() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Parapet", true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ARCHERY, self)
                && PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, null, 2, Keyword.MACHINE) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove 2 tokens from a machine";
                        }
                    });
            possibleEffects.add(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.SHADOW, -3), Phase.ARCHERY) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Make the minion archery total -3";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
