package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * •Aragorn, Uniter of Free Peoples
 * Gondor	Companion • Man
 * 8	4	8
 * Fellowship: Exert an Elf to Heal Aragorn.
 * Skirmish: Exert a Dwarf to make Aragorn damage +1.
 * Regroup: Exert two unbound Hobbits to make the Shadow player discard a minion (limit once per phase).
 */
public class Card20_176 extends AbstractCompanion {
    public Card20_176() {
        super(4, 8, 4, 8, Culture.GONDOR, Race.MAN, null, "Aragorn", "Uniter of Free Peoples", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Race.ELF)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
            action.appendEffect(
                    new HealCharactersEffect(self, self));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
            && PlayConditions.canExert(self, game, Race.DWARF)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.DAMAGE, 1)));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, 1, 2, Race.HOBBIT, Filters.unboundCompanion)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.HOBBIT, Filters.unboundCompanion));
            action.appendEffect(
                    new CheckPhaseLimitEffect(action, self, 1, Phase.REGROUP,
                            new ChooseOpponentEffect(playerId) {
                                @Override
                                protected void opponentChosen(String opponentId) {
                                    action.appendEffect(
                                            new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, 1, 1, CardType.MINION));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
