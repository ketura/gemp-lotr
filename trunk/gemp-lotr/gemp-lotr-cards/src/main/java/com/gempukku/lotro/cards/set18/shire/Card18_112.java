package com.gempukku.lotro.cards.set18.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Tale. When there are 4 [SHIRE] tokens here, discard this condition from play.
 * Response: If a [SHIRE] condition or [SHIRE] possession is about to be discarded from play by a Shadow card,
 * add a [SHIRE] token here to prevent that.
 */
public class Card18_112 extends AbstractPermanent {
    public Card18_112() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Scouring of the Shire");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getGameState().getTokenCount(self, Token.SHIRE) >= 4) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingDiscardedBy(effect, game, Side.SHADOW, Culture.SHIRE, Filters.or(CardType.CONDITION, CardType.POSSESSION))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTokenEffect(self, self, Token.SHIRE));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (DiscardCardsFromPlayEffect) effect, playerId, "Choose card to prevent discarding of", Culture.SHIRE, Filters.or(CardType.CONDITION, CardType.POSSESSION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
