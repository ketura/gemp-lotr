package com.gempukku.lotro.cards.set17.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 11
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. Each time the Free Peoples player heals a companion, you may add a burden.
 * Maneuver: Exert Ulaire Enquea twice to discard a condition (or two conditions if you can spot 5 Free Peoples
 * player's conditions).
 */
public class Card17_140 extends AbstractMinion {
    public Card17_140() {
        super(6, 11, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.enquea, "Duplicitous Lieutenant", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachHealed(game, effectResult, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfExert(self, 2, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendCost(
                    new SelfExertEffect(self));
            int count = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), 5, Side.FREE_PEOPLE, CardType.CONDITION) ? 2 : 1;
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, count, count, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
