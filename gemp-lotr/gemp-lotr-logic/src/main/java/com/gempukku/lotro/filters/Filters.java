package com.gempukku.lotro.filters;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CompletePhysicalCardVisitor;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardVisitor;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.*;

public class Filters {
    public static boolean canSpot(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        SpotFilterCardInPlayVisitor visitor = new SpotFilterCardInPlayVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(visitor);
        return visitor.isSpotted();
    }

    public static int countSpottable(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        GetCardsMatchingFilterVisitor visitor = new GetCardsMatchingFilterVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(visitor);
        return modifiersQuerying.getSpotCount(gameState, filter, visitor.getCounter());
    }

    public static Collection<PhysicalCard> filterActive(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        GetCardsMatchingFilterVisitor getCardsMatchingFilter = new GetCardsMatchingFilterVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(getCardsMatchingFilter);
        return getCardsMatchingFilter.getPhysicalCards();
    }

    public static Collection<PhysicalCard> filter(Collection<? extends PhysicalCard> cards, GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        List<PhysicalCard> result = new LinkedList<PhysicalCard>();
        for (PhysicalCard card : cards)
            if (filter.accepts(gameState, modifiersQuerying, card))
                result.add(card);
        return result;
    }

    public static PhysicalCard findFirstActive(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        SpotFilterCardInPlayVisitor visitor = new SpotFilterCardInPlayVisitor(gameState, modifiersQuerying, Filters.and(filters));
        gameState.iterateActiveCards(visitor);
        return visitor.getCard();
    }

    public static int countActive(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        GetCardsMatchingFilterVisitor matchingFilterVisitor = new GetCardsMatchingFilterVisitor(gameState, modifiersQuerying, Filters.and(filters));
        gameState.iterateActiveCards(matchingFilterVisitor);
        return matchingFilterVisitor.getCounter();
    }

    // Filters available

    public static Filter canBeAssignedToSkirmish() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                if (physicalCard.getBlueprint().getSide() == Side.SHADOW) {
                    for (Skirmish assignment : gameState.getAssignments()) {
                        if (assignment.getFellowshipCharacter() == physicalCard
                                || assignment.getShadowCharacters().contains(physicalCard))
                            return false;
                    }
                }
                return modifiersQuerying.canBeAssignedToSkirmish(gameState, physicalCard);
            }
        };
    }

    public static Filter weapon() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.hasKeyword(gameState, physicalCard, Keyword.HAND_WEAPON)
                        || modifiersQuerying.hasKeyword(gameState, physicalCard, Keyword.RANGED_WEAPON);
            }
        };
    }

    public static Filter inSkirmish() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                Skirmish skirmish = gameState.getSkirmish();
                if (skirmish != null) {
                    return (skirmish.getFellowshipCharacter() == physicalCard)
                            || skirmish.getShadowCharacters().contains(physicalCard);
                }
                return false;
            }
        };
    }

    public static Filter inSkirmishAgainst(final Filter againstFilter) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                Skirmish skirmish = gameState.getSkirmish();
                if (skirmish != null && skirmish.getFellowshipCharacter() != null) {
                    return (skirmish.getFellowshipCharacter() == physicalCard && Filters.filter(skirmish.getShadowCharacters(), gameState, modifiersQuerying, againstFilter).size() > 0)
                            || (skirmish.getShadowCharacters().contains(physicalCard) && againstFilter.accepts(gameState, modifiersQuerying, skirmish.getFellowshipCharacter()));
                }
                return false;
            }
        };
    }

    public static Filter canExert(final PhysicalCard source) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getVitality(gameState, physicalCard) > 1
                        && modifiersQuerying.canBeExerted(gameState, source, physicalCard);
            }
        };
    }

    public static Filter exhausted() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getVitality(gameState, physicalCard) == 1;
            }
        };
    }

    public static Filter notAssigned() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                for (Skirmish assignment : gameState.getAssignments()) {
                    if (assignment.getFellowshipCharacter() == physicalCard
                            || assignment.getShadowCharacters().contains(physicalCard))
                        return false;
                }
                return true;
            }
        };
    }

    public static Filter playable(final LotroGame game) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().checkPlayRequirements(physicalCard.getOwner(), game, physicalCard, 0);
            }
        };
    }

    public static Filter playable(final LotroGame game, final int twilightModifier) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().checkPlayRequirements(physicalCard.getOwner(), game, physicalCard, twilightModifier);
            }
        };
    }

    public static Filter any() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return true;
            }
        };
    }

    public static Filter none() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return false;
            }
        };
    }

    public static Filter isUnique() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().isUnique();
            }
        };
    }

    public static Filter signet(final Signet signet) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getBlueprint().getSignet() == signet);
            }
        };
    }

    public static Filter race(final Race race) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                LotroCardBlueprint blueprint = physicalCard.getBlueprint();
                CardType cardType = blueprint.getCardType();
                return (cardType == CardType.COMPANION || cardType == CardType.ALLY || cardType == CardType.MINION)
                        && blueprint.getRace() == race;
            }
        };
    }

    public static Filter side(final Side side) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getSide() == side;
            }
        };
    }

    public static Filter owner(final String playerId) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getOwner() != null && physicalCard.getOwner().equals(playerId);
            }
        };
    }

    public static Filter siteNumber(final int siteNumber) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getSiteNumber() == siteNumber;
            }
        };
    }

    public static Filter hasAttached(final Filter filter) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                List<PhysicalCard> physicalCardList = gameState.getAttachedCards(physicalCard);
                return (Filters.filter(physicalCardList, gameState, modifiersQuerying, filter).size() > 0);
            }
        };
    }

    public static Filter not(final Filter filter) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return !filter.accepts(gameState, modifiersQuerying, physicalCard);
            }
        };
    }

    public static Filter sameCard(final PhysicalCard card) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard == card);
            }
        };
    }

    public static Filter in(final Collection<PhysicalCard> cards) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return cards.contains(physicalCard);
            }
        };
    }

    public static Filter zone(final Zone zone) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getZone() == zone;
            }
        };
    }

    public static Filter wounded() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return gameState.getWounds(physicalCard) > 0;
            }
        };
    }

    public static Filter cardId(final int cardId) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getCardId() == cardId;
            }
        };
    }

    public static Filter name(final String name) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getName() != null && physicalCard.getBlueprint().getName().equals(name);
            }
        };
    }

    public static Filter type(final CardType cardType) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getBlueprint().getCardType() == cardType);
            }
        };
    }

    public static Filter hasAttached(final PhysicalCard attachment) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard == attachment.getAttachedTo();
            }
        };
    }

    public static Filter attachedTo(final Filter filter) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getAttachedTo() != null && filter.accepts(gameState, modifiersQuerying, physicalCard.getAttachedTo());
            }
        };
    }

    public static Filter culture(final Culture culture) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getBlueprint().getCulture() == culture);
            }
        };
    }

    public static Filter keyword(final Keyword keyword) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.hasKeyword(gameState, physicalCard, keyword);
            }
        };
    }

    public static Filter and(final Filter... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                for (Filter filter : filters) {
                    if (!filter.accepts(gameState, modifiersQuerying, physicalCard))
                        return false;
                }
                return true;
            }
        };
    }

    public static Filter and(final Filter[] filters1, final Filter... filters2) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                for (Filter filter : filters1) {
                    if (!filter.accepts(gameState, modifiersQuerying, physicalCard))
                        return false;
                }
                for (Filter filter : filters2) {
                    if (!filter.accepts(gameState, modifiersQuerying, physicalCard))
                        return false;
                }
                return true;
            }
        };
    }

    public static Filter or(final Filter... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                for (Filter filter : filters) {
                    if (filter.accepts(gameState, modifiersQuerying, physicalCard))
                        return true;
                }
                return false;
            }
        };
    }

    private static class SpotFilterCardInPlayVisitor implements PhysicalCardVisitor {
        private GameState _gameState;
        private ModifiersQuerying _modifiersQuerying;
        private Filter _filter;
        private PhysicalCard _card;

        private SpotFilterCardInPlayVisitor(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter) {
            _gameState = gameState;
            _modifiersQuerying = modifiersQuerying;
            _filter = filter;
        }

        @Override
        public boolean visitPhysicalCard(PhysicalCard physicalCard) {
            if (_filter.accepts(_gameState, _modifiersQuerying, physicalCard)) {
                _card = physicalCard;
                return true;
            }
            return false;
        }

        public boolean isSpotted() {
            return _card != null;
        }

        public PhysicalCard getCard() {
            return _card;
        }
    }

    private static class GetCardsMatchingFilterVisitor extends CompletePhysicalCardVisitor {
        private GameState _gameState;
        private ModifiersQuerying _modifiersQuerying;
        private Filter _filter;

        private Set<PhysicalCard> _physicalCards = new HashSet<PhysicalCard>();

        private GetCardsMatchingFilterVisitor(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter) {
            _gameState = gameState;
            _modifiersQuerying = modifiersQuerying;
            _filter = filter;
        }

        @Override
        protected void doVisitPhysicalCard(PhysicalCard physicalCard) {
            if (_filter.accepts(_gameState, _modifiersQuerying, physicalCard))
                _physicalCards.add(physicalCard);
        }

        public int getCounter() {
            return _physicalCards.size();
        }

        public Set<PhysicalCard> getPhysicalCards() {
            return _physicalCards;
        }
    }
}
