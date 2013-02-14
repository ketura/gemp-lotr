package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * 2
 * War Cry of Morannon
 * Sauron	Event • Skirmish
 * Make a [Sauron] minion strength +2 (and damage +1 if you cannot spot 2 Free Peoples cultures)
 */
public class Card20_380 extends AbstractEvent {
    public Card20_380() {
        super(Side.SHADOW, 2, Culture.SAURON, "War Cry of Morannon", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.SAURON, CardType.MINION) {
                    @Override
                    protected void selectedCharacterCallback(PhysicalCard selectedCharacter) {
                        if (!PlayConditions.canSpotFPCultures(game, 2, playerId))
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, selectedCharacter, Keyword.DAMAGE, 1)));
                    }
                });
        return action;
    }
}
