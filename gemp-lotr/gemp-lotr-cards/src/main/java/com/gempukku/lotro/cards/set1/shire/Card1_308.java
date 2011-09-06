package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Add 1 burden to wound each minion skirmishing the Ring-bearer.
 */
public class Card1_308 extends AbstractEvent {
    public Card1_308() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Power According to His Stature", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return true;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addCost(new AddBurdenEffect(playerId));
        Skirmish skirmish = game.getGameState().getSkirmish();
        if (skirmish != null) {
            if (game.getModifiersQuerying().hasKeyword(game.getGameState(), skirmish.getFellowshipCharacter(), Keyword.RING_BEARER)) {
                for (PhysicalCard minion : skirmish.getShadowCharacters()) {
                    action.addEffect(new WoundCharacterEffect(minion));
                }
            }
        }
        return action;
    }
}
