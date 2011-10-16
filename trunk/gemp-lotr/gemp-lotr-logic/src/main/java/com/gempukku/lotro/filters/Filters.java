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
    public static boolean canSpot(GameState gameState, ModifiersQuerying modifiersQuerying, Filterable... filters) {
        Filter filter = Filters.and(filters);
        SpotFilterCardInPlayVisitor visitor = new SpotFilterCardInPlayVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(visitor);
        return visitor.isSpotted();
    }

    public static int countSpottable(GameState gameState, ModifiersQuerying modifiersQuerying, Filterable... filters) {
        Filter filter = Filters.and(filters);
        GetCardsMatchingFilterVisitor visitor = new GetCardsMatchingFilterVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(visitor);
        return modifiersQuerying.getSpotCount(gameState, filter, visitor.getCounter());
    }

    public static Collection<PhysicalCard> filterActive(GameState gameState, ModifiersQuerying modifiersQuerying, Filterable... filters) {
        Filter filter = Filters.and(filters);
        GetCardsMatchingFilterVisitor getCardsMatchingFilter = new GetCardsMatchingFilterVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(getCardsMatchingFilter);
        return getCardsMatchingFilter.getPhysicalCards();
    }

    public static Collection<PhysicalCard> filter(Collection<? extends PhysicalCard> cards, GameState gameState, ModifiersQuerying modifiersQuerying, Filterable... filters) {
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

    public static Filter possessionClass(final PossessionClass possessionClass) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getPossessionClass() == possessionClass;
            }
        };
    }

    public static Filter hasToken(final Token token) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return gameState.getTokenCount(physicalCard, token) > 0;
            }
        };
    }

    public static Filter canBeAssignedToSkirmishByEffect(final Side sidePlayer) {
        return Filters.and(
                notAssignedToSkirmish,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.canBeAssignedToSkirmish(gameState, sidePlayer, physicalCard);
                    }
                });
    }

    public static Filter canBeAssignedToSkirmish(final Side sidePlayer) {
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
                return modifiersQuerying.canBeAssignedToSkirmish(gameState, sidePlayer, physicalCard);
            }
        };
    }

    public static Filter siteBlock(final Block block) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getSiteBlock() == block;
            }
        };
    }

    public static final Filter weapon = Filters.or(Filters.possessionClass(PossessionClass.HAND_WEAPON), Filters.possessionClass(PossessionClass.RANGED_WEAPON));

    public static final Filter inSkirmish = new Filter() {
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

    public static final Filter canTakeWound = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return modifiersQuerying.canTakeWound(gameState, physicalCard);
        }
    };

    public static final Filter exhausted = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return modifiersQuerying.getVitality(gameState, physicalCard) == 1;
        }
    };

    public static Filter inSkirmishAgainst(final Filterable... againstFilter) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                Skirmish skirmish = gameState.getSkirmish();
                if (skirmish != null && skirmish.getFellowshipCharacter() != null) {
                    return (skirmish.getFellowshipCharacter() == physicalCard && Filters.filter(skirmish.getShadowCharacters(), gameState, modifiersQuerying, againstFilter).size() > 0)
                            || (skirmish.getShadowCharacters().contains(physicalCard) && Filters.and(againstFilter).accepts(gameState, modifiersQuerying, skirmish.getFellowshipCharacter()));
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

    public static final Filter notAssignedToSkirmish = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            for (Skirmish assignment : gameState.getAssignments()) {
                if (assignment.getFellowshipCharacter() == physicalCard
                        || assignment.getShadowCharacters().contains(physicalCard))
                    return false;
            }
            Skirmish skirmish = gameState.getSkirmish();
            if (skirmish != null) {
                if (skirmish.getFellowshipCharacter() == physicalCard
                        || skirmish.getShadowCharacters().contains(physicalCard))
                    return false;
            }
            return true;
        }
    };

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

    public static final Filter any = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return true;
        }
    };

    public static final Filter none = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return false;
        }
    };

    public static final Filter unique = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return physicalCard.getBlueprint().isUnique();
        }
    };

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

    public static Filter isAllyHome(final int siteNumber, final Block siteBlock) {
        return Filters.and(
                Filters.type(CardType.ALLY),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return physicalCard.getBlueprint().isAllyAtHome(siteNumber, siteBlock);
                    }
                });
    }

    public static final Filter allyAtHome = Filters.and(
            Filters.type(CardType.ALLY),
            new Filter() {
                @Override
                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                    return physicalCard.getBlueprint().isAllyAtHome(gameState.getCurrentSiteNumber(), gameState.getCurrentSiteBlock());
                }
            });

    public static Filter siteNumber(final int siteNumber) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getSiteNumber() == siteNumber;
            }
        };
    }

    public static Filter hasAttached(final Filter... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                List<PhysicalCard> physicalCardList = gameState.getAttachedCards(physicalCard);
                return (Filters.filter(physicalCardList, gameState, modifiersQuerying, filters).size() > 0);
            }
        };
    }

    public static Filter hasStacked(final Filter filter) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                List<PhysicalCard> physicalCardList = gameState.getStackedCards(physicalCard);
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

    public static final Filter wounded = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return gameState.getWounds(physicalCard) > 0;
        }
    };

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

    public static Filter attachedTo(final Filterable... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getAttachedTo() != null && Filters.and(filters).accepts(gameState, modifiersQuerying, physicalCard.getAttachedTo());
            }
        };
    }

    public static Filter siteControlledByShadowPlayer(final String fellowshipPlayer) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getCardType() == CardType.SITE && physicalCard.getCardController() != null && !physicalCard.getCardController().equals(fellowshipPlayer);
            }
        };
    }

    public static Filter siteControlled(final String playerId) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getCardType() == CardType.SITE && playerId.equals(physicalCard.getCardController());
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

    public static Filter and(final Filterable... filters) {
        Filter[] filtersInt = convertToFilters(filters);
        return andInternal(filtersInt);
    }

    public static Filter or(final Filterable... filters) {
        Filter[] filtersInt = convertToFilters(filters);
        return orInternal(filtersInt);
    }

    private static Filter[] convertToFilters(Filterable... filters) {
        Filter[] filtersInt = new Filter[filters.length];
        for (int i = 0; i < filtersInt.length; i++)
            filtersInt[i] = changeToFilter(filters[i]);
        return filtersInt;
    }

    private static Filter changeToFilter(Filterable filter) {
        if (filter instanceof Filter)
            return (Filter) filter;
        else if (filter instanceof CardType)
            return Filters.type((CardType) filter);
        else if (filter instanceof Culture)
            return Filters.culture((Culture) filter);
        else if (filter instanceof Keyword)
            return Filters.keyword((Keyword) filter);
        else if (filter instanceof PossessionClass)
            return Filters.possessionClass((PossessionClass) filter);
        else if (filter instanceof Race)
            return Filters.race((Race) filter);
        else if (filter instanceof Side)
            return Filters.side((Side) filter);
        else if (filter instanceof Signet)
            return Filters.signet((Signet) filter);
        else if (filter instanceof Zone)
            return Filters.zone((Zone) filter);
        else
            throw new IllegalArgumentException("Unknown type of filterable: " + filter);
    }

    private static Filter andInternal(final Filter... filters) {
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

    public static Filter and(final Filterable[] filters1, final Filterable... filters2) {
        final Filter[] newFilters1 = convertToFilters(filters1);
        final Filter[] newFilters2 = convertToFilters(filters2);
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                for (Filter filter : newFilters1) {
                    if (!filter.accepts(gameState, modifiersQuerying, physicalCard))
                        return false;
                }
                for (Filter filter : newFilters2) {
                    if (!filter.accepts(gameState, modifiersQuerying, physicalCard))
                        return false;
                }
                return true;
            }
        };
    }

    private static Filter orInternal(final Filter... filters) {
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

    public static final Filter unboundCompanion = Filters.and(Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BOUND)));
    public static final Filter roaminMinion = Filters.and(Filters.type(CardType.MINION), Filters.keyword(Keyword.ROAMING));
    public static final Filter mounted = Filters.hasAttached(Filters.possessionClass(PossessionClass.MOUNT));

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
