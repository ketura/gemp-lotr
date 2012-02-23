package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 6
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. Hunter 4 (While skirmishing a non-hunter character, this character is strength +4.).
 * Each time this minion is assigned to a non-hunter companion, you may heal a hunter.
 */
public class Card17_136 extends AbstractMinion {
    public Card17_136() {
        super(3, 6, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Warrior");
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.HUNTER, 4);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, null, Filters.and(CardType.COMPANION, Filters.not(Keyword.HUNTER)), self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Keyword.HUNTER));
            return Collections.singletonList(action);
        }
        return null;
    }
}
