package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.PlayersCantUseCardPhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * 3
 * Orc Skulker
 * Minion • Orc
 * 9	3	6
 * Lurker.
 * Companions of the same culture as the companion assigned to skirmish this minion may not use skirmish special abilities.
 * http://lotrtcg.org/coreset/sauron/orcskulker(r1).png
 */
public class Card20_370 extends AbstractMinion {
    public Card20_370() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Skulker");
        addKeyword(Keyword.LURKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new PlayersCantUseCardPhaseSpecialAbilitiesModifier(self, Phase.SKIRMISH,
                CardType.COMPANION,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        final Skirmish skirmish = gameState.getSkirmish();
                        if (skirmish != null) {
                            final PhysicalCard fellowshipCharacter = skirmish.getFellowshipCharacter();
                            if (fellowshipCharacter != null)
                                if (fellowshipCharacter.getBlueprint().getCardType() == CardType.COMPANION)
                                    return fellowshipCharacter.getBlueprint().getCulture() == physicalCard.getBlueprint().getCulture();
                        }

                        return false;
                    }
                });
    }
}
