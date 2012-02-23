package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

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
 * Game Text: Fierce. Each time a player plays an event in a skirmish involving this minion, that player may exert
 * a non-hunter character.
 */
public class Card17_131 extends AbstractMinion {
    public Card17_131() {
        super(3, 6, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Slayer");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.EVENT)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            final PhysicalCard playedCard = playResult.getPlayedCard();
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new OptionalEffect(action, playedCard.getOwner(),
                            new ChooseAndExertCharactersEffect(action, playedCard.getOwner(), 1, 1, Filters.character, Filters.not(Keyword.HUNTER)) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Exert a non-hunter character";
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
