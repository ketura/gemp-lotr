package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTokenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.HasInitiativeModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot a Nazgul. Regroup: Discard a Nazgul to place a [WRAITH] token on this card. While there are
 * 3 [WRAITH] tokens on this card, the Shadow has initiative, regardless of the Free Peoples player's hand.
 */
public class Card7_184 extends AbstractPermanent {
    public Card7_184() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, "More Unbearable");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.NAZGUL);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new HasInitiativeModifier(self, new SpotCondition(self, Filters.hasToken(Token.WRAITH, 3)), Side.SHADOW));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.NAZGUL));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.WRAITH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
