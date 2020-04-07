package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.RuleUtils;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 5
 * Type: Companion • Ent
 * Strength: 12
 * Vitality: 4
 * Resistance: 6
 * Game Text: To play, spot 3 [GANDALF] companions. Each time Treebeard wins a skirmish, the first Shadow player must
 * exert X minions, where X is the difference between Treebeard’s strength and the losing character’s strength.
 */
public class Card15_038 extends AbstractCompanion {
    public Card15_038() {
        super(5, 12, 4, 6, Culture.GANDALF, Race.ENT, null, "Treebeard", "Enraged Shepherd", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.GANDALF, CardType.COMPANION);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            String firstShadowPlayer = GameUtils.getFirstShadowPlayer(game);

            int selfStr = game.getModifiersQuerying().getStrength(game, self);
            int oppStr = RuleUtils.getShadowSkirmishStrength(game);

            int diff = selfStr - oppStr;

            RequiredTriggerAction action = new RequiredTriggerAction(self);
            if (diff > 0)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, firstShadowPlayer, diff, diff, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
