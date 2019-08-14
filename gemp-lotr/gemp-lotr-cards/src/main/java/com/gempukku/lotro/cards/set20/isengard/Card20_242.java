package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * 1
 * Wizard’s Guile
 * Event • Maneuver
 * Spell.
 * Until the regroup phase, Saruman is enduring.
 * http://lotrtcg.org/coreset/isengard/wizardsguile(r1).png
 */
public class Card20_242 extends AbstractEvent {
    public Card20_242() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Wizard's Guile", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new KeywordModifier(self, Filters.saruman, Keyword.ENDURING), Phase.REGROUP));
        return action;
    }
}
