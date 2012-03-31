package com.gempukku.lotro.cards.set17.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. Each time the Witch-king wins a fierce skirmish, you may exert a companion for each companion you
 * can spot over 4.
 */
public class Card17_144 extends AbstractMinion {
    public Card17_144() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, "The Witch-king", "Conqueror of Arthedain", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)
                && game.getGameState().isFierceSkirmishes()) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) - 4;
            for (int i = 0; i < count; i++)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
