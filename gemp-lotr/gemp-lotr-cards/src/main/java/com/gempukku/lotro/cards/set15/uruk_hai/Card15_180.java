package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Each unwounded hunter [URUK-HAI] minion is strength +1. Response: If your hunter minion is about to take
 * a wound during a skirmish, discard that minion to exert a non-hunter companion.
 */
public class Card15_180 extends AbstractPermanent {
    public Card15_180() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "With All Possible Speed", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Culture.URUK_HAI, CardType.MINION, Keyword.HUNTER, Filters.unwounded), 1);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.owner(playerId), CardType.MINION, Keyword.HUNTER)
                && PlayConditions.isPhase(game, Phase.SKIRMISH)) {
            ActivateCardAction action = new ActivateCardAction(self);
            final Collection<PhysicalCard> woundedCharacters = ((WoundCharactersEffect) effect).getAffectedCardsMinusPrevented(game);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.owner(playerId), CardType.MINION, Keyword.HUNTER, Filters.in(woundedCharacters)));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Keyword.HUNTER)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
