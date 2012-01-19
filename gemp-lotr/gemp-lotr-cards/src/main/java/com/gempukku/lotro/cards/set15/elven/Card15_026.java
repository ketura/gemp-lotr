package com.gempukku.lotro.cards.set15.elven;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Follower
 * Resistance: +1
 * Game Text: Aid - (1). (At the start of the maneuver phase, you may add(1) to transfer this to a companion.)
 * Each time you transfer this to a companion, you may exert an Elf to make the fellowship’s current site a forest until
 * the end of the turn.
 */
public class Card15_026 extends AbstractFollower {
    public Card15_026() {
        super(Side.FREE_PEOPLE, 2, 0, 0, 1, Culture.ELVEN, "Uruviel", true);
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, PhysicalCard self) {
        return new AddTwilightEffect(self, 1);
    }

    @Override
    protected List<OptionalTriggerAction> getExtraOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, CardType.COMPANION)
                && PlayConditions.canExert(self, game, Race.ELF)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new KeywordModifier(self, game.getGameState().getCurrentSite(), Keyword.FOREST)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
