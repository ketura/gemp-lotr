package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. When you play this minion from a site you control, add 2 threats.
 */
public class Card7_272 extends AbstractMinion {
    public Card7_272() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Engineer");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            if (playResult.getPlayedFrom() == Zone.STACKED && Filters.siteControlled(self.getOwner()).accepts(game.getGameState(), game.getModifiersQuerying(), playResult.getAttachedOrStackedPlayedFrom())) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddThreatsEffect(self.getOwner(), self, 2));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
