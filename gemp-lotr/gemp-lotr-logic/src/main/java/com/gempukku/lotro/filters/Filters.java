package com.gempukku.lotro.filters;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CompletePhysicalCardVisitor;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardVisitor;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.LinkedList;
import java.util.List;

public class Filters {
    public static boolean canSpotAnywhere(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        SpotFilterCardInPlayVisitor visitor = new SpotFilterCardInPlayVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateAllCards(visitor);
        return visitor.isSpotted();
    }

    public static List<PhysicalCard> filterAnywhere(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        GetCardsVisitor getCards = new GetCardsVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateAllCards(getCards);
        return getCards.getPhysicalCards();
    }

    public static boolean canSpot(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        SpotFilterCardInPlayVisitor visitor = new SpotFilterCardInPlayVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(visitor);
        return visitor.isSpotted();
    }

    public static List<PhysicalCard> filterActive(GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
        Filter filter = Filters.and(filters);
        GetCardsVisitor getCards = new GetCardsVisitor(gameState, modifiersQuerying, filter);
        gameState.iterateActiveCards(getCards);
        return getCards.getPhysicalCards();
    }

    public static List<PhysicalCard> filter(List<? extends PhysicalCard> cards, GameState gameState, ModifiersQuerying modifiersQuerying, Filter... filters) {
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
        GetCardsVisitor visitor = new GetCardsVisitor(gameState, modifiersQuerying, Filters.and(filters));
        gameState.iterateActiveCards(visitor);
        return visitor.getCounter();
    }

    public static Filter any() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return true;
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
                return physicalCard.getOwner().equals(playerId);
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

    public static Filter canExert() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (gameState.getWounds(physicalCard) < modifiersQuerying.getVitality(gameState, physicalCard) - 1);
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

    public static Filter in(final List<PhysicalCard> cards) {
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
                return (physicalCard.getBlueprint().getName().equals(name));
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

    public static Filter attachedTo(final PhysicalCard attachment) {
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
                return filter.accepts(gameState, modifiersQuerying, physicalCard.getAttachedTo());
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

    private static class GetCardsVisitor extends CompletePhysicalCardVisitor {
        private GameState _gameState;
        private ModifiersQuerying _modifiersQuerying;
        private Filter _filter;

        private List<PhysicalCard> _physicalCards = new LinkedList<PhysicalCard>();

        private GetCardsVisitor(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter) {
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

        public List<PhysicalCard> getPhysicalCards() {
            return _physicalCards;
        }
    }
}
