package com.gempukku.lotro.cards.set32.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Shadow: Remove (2) to play a [SAURON] card from your draw deck or discard pile (limit
 * once per phase). Skirmish: Remove a doubt or exert Sauron to make an Orc strength +1 (limit +3)
 */
public class Card32_037 extends AbstractPermanent {
    public Card32_037() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, "Danger Wrapped in Shadows");
    }
    
    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 2)
            && PlayConditions.checkPhaseLimit(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            Filterable sauronCardFilter = Filters.and(Culture.SAURON, Filters.or(Filters.not(CardType.EVENT), Filters.and(CardType.EVENT, Keyword.REGROUP)));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            action.appendCost(
                    new RemoveTwilightEffect(2));
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, sauronCardFilter) {
                @Override
                public String getText(LotroGame game) {
                    return "Play a SAURON card from your draw deck";
                }
            });
            if (PlayConditions.canPlayFromDiscard(playerId, game, sauronCardFilter)) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, sauronCardFilter) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play a SAURON card from your discard pile";
                    }
                });
            }
            action.appendEffect(
                    new IncrementPhaseLimitEffect(self, 1));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && (game.getGameState().getBurdens() >= 1
                 || PlayConditions.canExert(self, game, Filters.name("Sauron")))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveBurdenEffect(playerId, self, 1) {
                @Override
                public String getText(LotroGame game) {
                    return "Remove a doubt"; 
                }
            });
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Sauron")) {
                @Override
                public String getText(LotroGame game) {
                    return "Exert Sauron";
                }
            });
            action.appendCost(new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new CheckPhaseLimitEffect(action, self, 3, Phase.SKIRMISH,
                            new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Race.ORC)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
