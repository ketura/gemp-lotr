package com.gempukku.lotro.cards.set15.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CardPhaseLimitEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Archer. Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * Archery: Exert Legolas to make the fellowship archery total +1 for each hunter minion you can spot. (limit +3)
 */
public class Card15_019 extends AbstractCompanion {
    public Card15_019() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, null, "Legolas", "Of the Woodland Realm", true);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));

            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, null,
                                    new CardPhaseLimitEvaluator(game, self, Phase.ARCHERY, 3,
                                            new CountActiveEvaluator(CardType.MINION, Keyword.HUNTER))), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
