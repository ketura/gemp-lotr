package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 3
 * Type: Site
 * Site: 6K
 * Game Text: Sanctuary. If the fellowship moves from this site during the regroup phase, heal each hobbit companion.
 */
public class Card7_352 extends AbstractSite {
    public Card7_352() {
        super("Minas Tirith Third Circle", Block.KING, 6, 3, Direction.LEFT);

    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, self)
                && PlayConditions.isPhase(game, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, Race.HOBBIT, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }

}