package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Possession • Mount
 * Game Text: Bearer must be a [ROHAN] Man. At the start of each skirmish involving bearer, each minion skirmishing
 * bearer must exert. Skirmish: If bearer is Eowyn, exert her twice to wound a fierce minion she is skirmishing.
 */
public class Card7_260 extends AbstractAttachableFPPossession {
    public Card7_260() {
        super(2, 0, 0, Culture.ROHAN, PossessionClass.MOUNT, "Windfola", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && Filters.inSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(self, Filters.and(Filters.type(CardType.MINION), Filters.inSkirmishAgainst(Filters.hasAttached(self)))));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && self.getAttachedTo().getBlueprint().getName().equals("Eowyn")
                && PlayConditions.canExert(self, game, 2, Filters.name("Eowyn"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Eowyn")));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Keyword.FIERCE, Filters.inSkirmishAgainst(Filters.name("Eowyn"))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
