package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Silinde, Mirkwood Escort
 * Elven	Ally • Elf • Rivendell
 * 5	2
 * If an exhausted Elf is about to take a wound, exert Silinde to prevent that wound.
 */
public class Card20_104 extends AbstractAlly {
    public Card20_104() {
        super(2, null, 0, 5, 2, Race.ELF, Culture.ELVEN, "Silinde", "Mirkwood Escort", true);
        addKeyword(Keyword.RIVENDELL);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.ELF, Filters.exhausted)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, self.getOwner(), "Choose Elf to prevent wound to", Race.ELF, Filters.exhausted));
            return Collections.singletonList(action);
        }
        return null;
    }
}
