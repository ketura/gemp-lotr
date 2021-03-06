package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 11
 * Vitality: 4
 * Site: 3
 * Game Text: Twilight. Each time Úlairë Enquëa wins a skirmish, you may exert him to wound the Ring-bearer once
 * (or twice if you spot 5 burdens).
 */
public class Card2_083 extends AbstractMinion {
    public Card2_083() {
        super(6, 11, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.enquea, "Ringwraith in Twilight", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)
                && PlayConditions.canExert(self, game, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.ringBearer));
            if (game.getGameState().getBurdens() >= 5) {
                action.appendEffect(
                        new WoundCharactersEffect(self, Filters.ringBearer));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
