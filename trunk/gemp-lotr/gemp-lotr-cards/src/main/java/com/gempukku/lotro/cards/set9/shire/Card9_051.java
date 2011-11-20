package com.gempukku.lotro.cards.set9.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 3
 * Type: Ally • Home 2
 * Strength: 4
 * Vitality: 6
 * Site: 2
 * Game Text: Each time the fellowship moves to a river, heal Goldberry and Tom Bombadil. Skirmish: If the fellowship is
 * at a river, exert Goldberry to make a companion strength +1.
 */
public class Card9_051 extends AbstractAlly {
    public Card9_051() {
        super(3, Block.FELLOWSHIP, 2, 4, 6, null, Culture.SHIRE, "Goldberry", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, Keyword.RIVER)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.or(Filters.name("Goldberry"), Filters.name("Tom Bombadil"))));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.and(Keyword.RIVER).accepts(game.getGameState(), game.getModifiersQuerying(), game.getGameState().getCurrentSite())
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
