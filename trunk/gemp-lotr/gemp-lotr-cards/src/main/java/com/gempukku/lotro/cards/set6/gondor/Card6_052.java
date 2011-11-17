package com.gempukku.lotro.cards.set6.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Fortification. Plays to your support area. When you play this condition, you may spot a [GONDOR] Man
 * to place 2 [GONDOR] tokens here. Skirmish: Discard this condition or remove a [GONDOR] token from here to make
 * a [GONDOR] Man strength +1.
 */
public class Card6_052 extends AbstractPermanent {
    public Card6_052() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Garrison of Osgiliath", true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.GONDOR, 2));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new DiscardCardsFromPlayEffect(self, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this condition";
                        }
                    });
            possibleCosts.add(
                    new RemoveTokenEffect(self, self, Token.GONDOR) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a GONDOR token from here";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Culture.GONDOR, Race.MAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
