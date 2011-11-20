package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 10
 * Vitality: 4
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Damage +1. Each time the fellowship moves, wound Gandalf.
 */
public class Card6_030 extends AbstractCompanion {
    public Card6_030() {
        super(4, 10, 4, 6, Culture.GANDALF, Race.WIZARD, Signet.ARAGORN, "Gandalf", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new WoundCharactersEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
