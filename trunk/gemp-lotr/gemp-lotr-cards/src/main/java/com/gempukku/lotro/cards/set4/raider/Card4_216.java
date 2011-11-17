package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a companion or ally loses a skirmish involving a [RAIDER] Man, you
 * may place a [RAIDER] token here. Archery: Heal a [RAIDER] archer for each [RAIDER] token here. Discard this condition.
 */
public class Card4_216 extends AbstractPermanent {
    public Card4_216() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.RAIDER, Zone.SUPPORT, "Arrow From the South", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmish(game, effectResult,
                Filters.and(
                        Filters.or(CardType.COMPANION, CardType.ALLY),
                        Filters.inSkirmishAgainst(Filters.and(Culture.RAIDER, Race.MAN))))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.RAIDER));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            int count = game.getGameState().getTokenCount(self, Token.RAIDER);
            for (int i = 0; i < count; i++)
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Culture.RAIDER, Keyword.ARCHER));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
