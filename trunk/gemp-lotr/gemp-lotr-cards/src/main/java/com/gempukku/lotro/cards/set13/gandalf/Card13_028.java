package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Follower
 * Game Text: Aid - (2). (At the start of the maneuver phase, you may add (2) to transfer this to a companion.) Each
 * time you transfer this to a companion, you may reinforce a [GANDALF] token. While you can spot a [GANDALF] companion,
 * each minion skirmishing bearer is strength -1.
 */
public class Card13_028 extends AbstractFollower {
    public Card13_028() {
        super(Side.FREE_PEOPLE, 2, 0, 0, 0, Culture.GANDALF, "Alatar", true);
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, PhysicalCard self) {
        return new AddTwilightEffect(self, 2);
    }

    @Override
    protected List<OptionalTriggerAction> getExtraOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.GANDALF));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))), new SpotCondition(Culture.GANDALF, CardType.COMPANION), -1);
    }
}
