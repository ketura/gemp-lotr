package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayPermanentAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Balin's Fate
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1C155
 * Game Text: To play, exert a [MORIA] Goblin.
 * Each time the Free Peoples player plays a [DWARVEN] skirmish event, he or she must exert a Dwarf.
 */
public class Card40_155 extends AbstractPermanent {
    public Card40_155() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.MORIA, "Balin's Fate", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Culture.MORIA, Race.GOBLIN);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.MORIA, Race.GOBLIN));
        return action;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Side.FREE_PEOPLE, Culture.DWARVEN, CardType.EVENT, Keyword.SKIRMISH)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, GameUtils.getFreePeoplePlayer(game), 1, 1, Race.DWARF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
