package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 1
 * Type: Site
 * Site: 2T
 * Game Text: Plains. Battleground. Each time an Uruk-hai is played, that minion must exert.
 */
public class Card4_335 extends AbstractSite {
    public Card4_335() {
        super("Uruk Camp", Block.TWO_TOWERS, 2, 1, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Race.URUK_HAI)) {
            PlayCardResult playCardResult = (PlayCardResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(self, playCardResult.getPlayedCard()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
