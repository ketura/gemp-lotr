package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: Southron. Ambush (1). When the Free Peoples player assigns this minion to a character and you have
 * initiative, you may wound that character.
 */
public class Card7_133 extends AbstractMinion {
    public Card7_133() {
        super(2, 6, 2, 4, Race.MAN, Culture.RAIDER, "Desert Runner");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.assigned(game, effectResult, Side.FREE_PEOPLE, Filters.any, self)
                && PlayConditions.hasInitiative(game, Side.SHADOW)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
