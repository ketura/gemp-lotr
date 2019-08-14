package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Tale. To play, spot Bilbo. Plays on any character. Limit 1 per character. Each time bearer skirmishes a
 * Troll or Uruk-hai, bearer is strength +3.
 */
public class Card1_314 extends AbstractAttachable {
    public Card1_314() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.SHIRE, null, "Stone Trolls");
        addKeyword(Keyword.TALE);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.or(CardType.COMPANION, CardType.ALLY), Filters.not(Filters.hasAttached(Filters.name(getName()))));
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.name("Bilbo"));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self,
                Filters.and(
                        Filters.hasAttached(self),
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                Skirmish skirmish = game.getGameState().getSkirmish();
                                return skirmish != null
                                        && skirmish.getFellowshipCharacter() == self.getAttachedTo()
                                        && Filters.filter(skirmish.getShadowCharacters(), game,
                                        Filters.or(
                                                Race.URUK_HAI,
                                                Race.TROLL)).size() > 0;
                            }
                        }), 3));
    }
}
