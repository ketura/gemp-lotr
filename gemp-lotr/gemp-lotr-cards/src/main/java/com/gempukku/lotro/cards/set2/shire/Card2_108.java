package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition
 * Strength: +1
 * Game Text: Tale. Bearer must be the Ring-bearer. Skirmish: Discard this condition to take off The One Ring or
 * to cancel a skirmish involving the Ring-bearer and a Nazgul.
 */
public class Card2_108 extends AbstractAttachable {
    public Card2_108() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.SHIRE, null, "O Elbereth! Gilthoniel!", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.RING_BEARER;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new TakeOffTheOneRingEffect() {
                        @Override
                        public String getText(LotroGame game) {
                            return "Take off The One Ring";
                        }
                    });
            possibleEffects.add(
                    new CancelRingBearerAndNazgulSkirmish(self.getAttachedTo()) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Cancel a skirmish";
                        }
                    });

            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));

            return Collections.singletonList(action);
        }
        return null;
    }

    private class CancelRingBearerAndNazgulSkirmish extends CancelSkirmishEffect {
        private PhysicalCard _attachedTo;

        private CancelRingBearerAndNazgulSkirmish(PhysicalCard attachedTo) {
            _attachedTo = attachedTo;
        }

        @Override
        public String getText(LotroGame game) {
            return "Cancel a skirmish involving the Ring-bearer and a Nazgul";
        }

        @Override
        public boolean isPlayableInFull(LotroGame game) {
            Skirmish skirmish = game.getGameState().getSkirmish();
            return (skirmish != null && skirmish.getFellowshipCharacter() == _attachedTo
                    && Filters.filter(skirmish.getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL).size() > 0);
        }
    }
}
