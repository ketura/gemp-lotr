package com.gempukku.lotro.cards.set31.gundabad;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 3
 * Game Text: Damage +1. Each time you play a mount, you may make the Free Peoples player exert a companion.
 */
public class Card31_023 extends AbstractMinion {
    public Card31_023() {
        super(4, 9, 3, 3, Race.ORC, Culture.SMAUG, "Azog", "The Defiler", true);
                addKeyword(Keyword.DAMAGE, 1);
		addKeyword(Keyword.WARG_RIDER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, PossessionClass.MOUNT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
