package com.gempukku.lotro.cards.set8.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CheckLimitEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Fellowship: Exert an Elf companion and either a [GANDALF] companion or a [DWARVEN] companion to add an
 * [ELVEN] token here. Archery: Remove an [ELVEN] token here to make the fellowship archery total +1 (limit +2).
 */
public class Card8_013 extends AbstractPermanent {
    public Card8_013() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Shake Off the Shadow");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Race.ELF, CardType.COMPANION)
                && PlayConditions.canExert(self, game, CardType.COMPANION, Filters.or(Culture.GANDALF, Culture.DWARVEN))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF, CardType.COMPANION));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.or(Culture.GANDALF, Culture.DWARVEN)));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ELVEN));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canRemoveTokens(game, Token.ELVEN, 1, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.ELVEN));
            action.appendEffect(
                    new CheckLimitEffect(action, self, 2, Phase.ARCHERY,
                            new AddUntilEndOfPhaseModifierEffect(
                                    new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 1), Phase.ARCHERY)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
