package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Osgiliath
 * Set: Second Edition
 * Side: None
 * Site Number: 7
 * Shadow Number: 7
 * Card Number: 1C302
 * Game Text: Battleground. At the end of the Shadow phase, the Shadow player may spot 5 Orcs to make the move limit -1 this turn.
 */
public class Card40_302 extends AbstractSite {
    public Card40_302() {
        super("Osgiliath", SitesBlock.SECOND_ED, 7, 7, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfPhase(game, effectResult, Phase.SHADOW)
                && GameUtils.isShadow(game, playerId)
                && PlayConditions.canSpot(game, 5, Race.ORC)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, -1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
