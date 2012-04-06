package com.gempukku.lotro.filters;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.CompletePhysicalCardVisitor;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.PhysicalCardVisitor;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.*;

public class Filters {
    private static final Map<CardType, Filter> _typeFilterMap = new HashMap<CardType, Filter>();
    private static final Map<PossessionClass, Filter> _possessionClassFilterMap = new HashMap<PossessionClass, Filter>();
    private static final Map<Signet, Filter> _signetFilterMap = new HashMap<Signet, Filter>();
    private static final Map<Race, Filter> _raceFilterMap = new HashMap<Race, Filter>();
    private static final Map<Zone, Filter> _zoneFilterMap = new HashMap<Zone, Filter>();
    private static final Map<Side, Filter> _sideFilterMap = new HashMap<Side, Filter>();
    private static final Map<Culture, Filter> _cultureFilterMap = new HashMap<Culture, Filter>();
    private static final Map<Keyword, Filter> _keywordFilterMap = new HashMap<Keyword, Filter>();

    static {
        for (Culture culture : Culture.values())
            _cultureFilterMap.put(culture, culture(culture));
        for (Side side : Side.values())
            _sideFilterMap.put(side, side(side));
        for (Zone zone : Zone.values())
            _zoneFilterMap.put(zone, zone(zone));
        for (Race race : Race.values())
            _raceFilterMap.put(race, race(race));
        for (Signet signet : Signet.values())
            _signetFilterMap.put(signet, signet(signet));
        for (PossessionClass possessionClass : PossessionClass.values())
            _possessionClassFilterMap.put(possessionClass, possessionClass(possessionClass));
        for (CardType cardType : CardType.values())
            _typeFilterMap.put(cardType, type(cardType));
        for (Keyword keyword : Keyword.values())
            _keywordFilterMap.put(keyword, keyword(keyword));

        // Some simple shortcuts for filters
        // Only companions can be rangers
        _keywordFilterMap.put(Keyword.RANGER, Filters.and(CardType.COMPANION, keyword(Keyword.RANGER)));
        // Only allies can be villagers
        _keywordFilterMap.put(Keyword.VILLAGER, Filters.and(CardType.ALLY, keyword(Keyword.VILLAGER)));

        // Minion groups
        _keywordFilterMap.put(Keyword.SOUTHRON, Filters.and(CardType.MINION, keyword(Keyword.SOUTHRON)));
        _keywordFilterMap.put(Keyword.EASTERLING, Filters.and(CardType.MINION, keyword(Keyword.EASTERLING)));
        _keywordFilterMap.put(Keyword.CORSAIR, Filters.and(CardType.MINION, keyword(Keyword.CORSAIR)));
        _keywordFilterMap.put(Keyword.TRACKER, Filters.and(CardType.MINION, keyword(Keyword.TRACKER)));
        _keywordFilterMap.put(Keyword.WARG_RIDER, Filters.and(CardType.MINION, keyword(Keyword.WARG_RIDER)));
        _keywordFilterMap.put(Keyword.BESIEGER, Filters.and(CardType.MINION, keyword(Keyword.BESIEGER)));
    }

    public static boolean canSpot(GameState gameState, ModifiersQuerying modifiersQuerying, Filterable... filters) {
        Filter filter = Filters.and(filters);
        SpotFilterCardInPlayVisitor visitor = new SpotFilterCardInPlayVisitor(gameState, modifiersQuerying, filter);
        return gameState.iterateActiveCards(visitor);
    }

    public static boolean canSpot(GameState gameState, ModifiersQuerying modifiersQuerying, int count, Filterable... filters) {
        Filter filter = Filters.and(filters);
        SpotCountFilterCardInPlayVisitor visitor = new SpotCountFilterCardInPlayVisitor(gameState, modifiersQuerying, count, filter);
        return gameState.iterateActiveCards(visitor);
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

    public static PhysicalCard findFirstActive(GameState gameState, ModifiersQuerying modifiersQuerying, Filterable... filters) {
        SpotFilterCardInPlayVisitor visitor = new SpotFilterCardInPlayVisitor(gameState, modifiersQuerying, Filters.and(filters));
        gameState.iterateActiveCards(visitor);
        return visitor.getCard();
    }

    public static int countActive(GameState gameState, ModifiersQuerying modifiersQuerying, Filterable... filters) {
        GetCardsMatchingFilterVisitor matchingFilterVisitor = new GetCardsMatchingFilterVisitor(gameState, modifiersQuerying, Filters.and(filters));
        gameState.iterateActiveCards(matchingFilterVisitor);
        return matchingFilterVisitor.getCounter();
    }

    // Filters available

    public static Filter canSpotCompanionWithStrengthAtLeast(final int strength) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return Filters.canSpot(gameState, modifiersQuerying, CardType.COMPANION, Filters.not(Filters.lessStrengthThan(strength)));
            }
        };
    }

    public static Filter lessVitalityThan(final int vitality) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getVitality(gameState, physicalCard) < vitality;
            }
        };
    }

    public static Filter moreVitalityThan(final int vitality) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getVitality(gameState, physicalCard) > vitality;
            }
        };
    }

    public static Filter maxResistance(final int resistance) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getResistance(gameState, physicalCard) <= resistance;
            }
        };
    }

    public static Filter minResistance(final int resistance) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getResistance(gameState, physicalCard) >= resistance;
            }
        };
    }

    public static Filter minVitality(final int vitality) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getVitality(gameState, physicalCard) >= vitality;
            }
        };
    }

    public static Filter lessStrengthThan(final int strength) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getStrength(gameState, physicalCard) < strength;
            }
        };
    }

    public static Filter lessStrengthThan(final PhysicalCard card) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.getStrength(gameState, physicalCard) < modifiersQuerying.getStrength(gameState, card);
            }
        };
    }


    private static Filter possessionClass(final PossessionClass possessionClass) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                final Set<PossessionClass> possessionClasses = physicalCard.getBlueprint().getPossessionClasses();
                return possessionClasses != null && possessionClasses.contains(possessionClass);
            }
        };
    }

    public static Filter hasAnyCultureTokens(final int count) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                Map<Token, Integer> tokens = gameState.getTokens(physicalCard);
                for (Map.Entry<Token, Integer> tokenCount : tokens.entrySet()) {
                    if (tokenCount.getKey().getCulture() != null)
                        if (tokenCount.getValue() >= count)
                            return true;
                }
                return false;
            }
        };
    }

    public static Filter printedTwilightCost(final int printedTwilightCost) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getTwilightCost() == printedTwilightCost;
            }
        };
    }

    public static Filter maxPrintedTwilightCost(final int printedTwilightCost) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getTwilightCost() <= printedTwilightCost;
            }
        };
    }

    public static Filter minPrintedTwilightCost(final int printedTwilightCost) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getTwilightCost() >= printedTwilightCost;
            }
        };
    }

    public static Filter hasToken(final Token token) {
        return hasToken(token, 1);
    }

    public static Filter hasToken(final Token token, final int count) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return gameState.getTokenCount(physicalCard, token) >= count;
            }
        };
    }

    public static Filter assignableToSkirmishAgainst(final Side assignedBySide, final Filterable againstFilter) {
        return assignableToSkirmishAgainst(assignedBySide, againstFilter, false, false);
    }

    public static Filter assignableToSkirmishAgainst(final Side assignedBySide, final Filterable againstFilter, final boolean ignoreUnassigned, final boolean allowAllyToSkirmish) {
        return Filters.and(
                assignableToSkirmish(assignedBySide, ignoreUnassigned, allowAllyToSkirmish),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        Map<PhysicalCard, Set<PhysicalCard>> currentAssignments = new HashMap<PhysicalCard, Set<PhysicalCard>>();
                        for (Assignment assignment : gameState.getAssignments())
                            currentAssignments.put(assignment.getFellowshipCharacter(), assignment.getShadowCharacters());

                        for (PhysicalCard card : Filters.filterActive(gameState, modifiersQuerying, againstFilter)) {
                            if (card.getBlueprint().getSide() != physicalCard.getBlueprint().getSide()
                                    && Filters.assignableToSkirmish(assignedBySide, ignoreUnassigned, allowAllyToSkirmish).accepts(gameState, modifiersQuerying, card)) {
                                Map<PhysicalCard, Set<PhysicalCard>> afterThatAssignment = new HashMap<PhysicalCard, Set<PhysicalCard>>(currentAssignments);
                                if (card.getBlueprint().getSide() == Side.FREE_PEOPLE) {
                                    if (afterThatAssignment.containsKey(card))
                                        afterThatAssignment.get(card).add(physicalCard);
                                    else
                                        afterThatAssignment.put(card, Collections.singleton(physicalCard));
                                } else {
                                    if (afterThatAssignment.containsKey(physicalCard))
                                        afterThatAssignment.get(physicalCard).add(card);
                                    else
                                        afterThatAssignment.put(physicalCard, Collections.singleton(card));
                                }
                                if (modifiersQuerying.isValidAssignments(gameState, assignedBySide, afterThatAssignment))
                                    return true;
                            }
                        }

                        return false;
                    }
                });
    }

    public static Filter assignableToSkirmish(final Side assignedBySide, final boolean ignoreUnassigned, final boolean allowAllyToSkirmish) {
        Filter assignableFilter = Filters.or(
                Filters.and(
                        CardType.ALLY,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                if (allowAllyToSkirmish)
                                    return true;
                                boolean allowedToSkirmish = modifiersQuerying.isAllyAllowedToParticipateInSkirmishes(gameState, assignedBySide, physicalCard);
                                if (allowedToSkirmish)
                                    return true;
                                boolean preventedByCard = modifiersQuerying.isAllyPreventedFromParticipatingInSkirmishes(gameState, assignedBySide, physicalCard);
                                if (preventedByCard)
                                    return false;
                                return physicalCard.getBlueprint().isAllyAtHome(gameState.getCurrentSiteNumber(), gameState.getCurrentSiteBlock());
                            }
                        }),
                Filters.and(
                        CardType.COMPANION,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return assignedBySide == Side.SHADOW || !modifiersQuerying.hasKeyword(gameState, physicalCard, Keyword.UNHASTY)
                                        || modifiersQuerying.isUnhastyCompanionAllowedToParticipateInSkirmishes(gameState, physicalCard);
                            }
                        }),
                Filters.and(
                        CardType.MINION,
                        Filters.notAssignedToSkirmish,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return (!gameState.isFierceSkirmishes()) || modifiersQuerying.hasKeyword(gameState, physicalCard, Keyword.FIERCE);
                            }
                        }));

        return Filters.and(
                assignableFilter,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        if (!ignoreUnassigned) {
                            boolean notAssignedToSkirmish = Filters.notAssignedToSkirmish.accepts(gameState, modifiersQuerying, physicalCard);
                            if (!notAssignedToSkirmish)
                                return false;
                        }
                        return modifiersQuerying.canBeAssignedToSkirmish(gameState, assignedBySide, physicalCard);
                    }
                });
    }

    public static Filter siteBlock(final Block block) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getSiteBlock() == block;
            }
        };
    }

    public static final Filter saruman = Filters.name("Saruman");
    public static final Filter witchKing = Filters.name(Names.witchKing);
    public static final Filter balrog = Filters.name("The Balrog");

    public static final Filter gollum = Filters.name("Gollum");
    public static final Filter smeagol = Filters.name("Smeagol");
    public static final Filter gollumOrSmeagol = Filters.or(gollum, smeagol);

    public static final Filter aragorn = Filters.name("Aragorn");
    public static final Filter gandalf = Filters.name("Gandalf");
    public static final Filter gimli = Filters.name("Gimli");
    public static final Filter arwen = Filters.name("Arwen");
    public static final Filter legolas = Filters.name("Legolas");
    public static final Filter boromir = Filters.name("Boromir");
    public static final Filter frodo = Filters.name("Frodo");
    public static final Filter sam = Filters.name("Sam");

    public static final Filter galadriel = Filters.name("Galadriel");

    public static final Filter weapon = Filters.or(PossessionClass.HAND_WEAPON, PossessionClass.RANGED_WEAPON);
    public static final Filter character = Filters.or(CardType.ALLY, CardType.COMPANION, CardType.MINION);

    public static final Filter ringBearer = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return gameState.getRingBearer(gameState.getCurrentPlayerId()) == physicalCard;
        }
    };

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

    public static final Filter canTakeWounds(final int count) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canTakeWounds(gameState, physicalCard, count) && modifiersQuerying.getVitality(gameState, physicalCard) >= count;
            }
        };
    }

    public static final Filter canBeDiscarded(final PhysicalCard source) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canBeDiscardedFromPlay(gameState, physicalCard, source);
            }
        };
    }

    public static final Filter canTakeWound = canTakeWounds(1);

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


    public static Filter canHeal =
            new Filter() {
                @Override
                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                    return gameState.getWounds(physicalCard) > 0 && modifiersQuerying.canBeHealed(gameState, physicalCard);
                }
            };

    public static final Filter notAssignedToSkirmish = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            for (Assignment assignment : gameState.getAssignments()) {
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

    public static final Filter assignedToSkirmish = Filters.not(Filters.notAssignedToSkirmish);

    public static final Filter assignedToSkirmishAgainst(final Filterable... againstFilters) {
        return Filters.or(Filters.assignedAgainst(againstFilters), Filters.inSkirmishAgainst(againstFilters));
    }

    public static final Filter assignedAgainst(final Filterable... againstFilters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                for (Assignment assignment : gameState.getAssignments()) {
                    if (assignment.getFellowshipCharacter() == physicalCard)
                        return Filters.filter(assignment.getShadowCharacters(), gameState, modifiersQuerying, againstFilters).size() > 0;
                    else if (assignment.getShadowCharacters().contains(physicalCard) && assignment.getFellowshipCharacter() != null)
                        return Filters.and(againstFilters).accepts(gameState, modifiersQuerying, assignment.getFellowshipCharacter());
                }
                return false;
            }
        };
    }

    public static Filter playable(final LotroGame game) {
        return playable(game, 0);
    }

    public static Filter playable(final LotroGame game, final int twilightModifier) {
        return playable(game, twilightModifier, false);
    }

    public static Filter playable(final LotroGame game, final int twilightModifier, final boolean ignoreRoamingPenalty) {
        return playable(game, twilightModifier, ignoreRoamingPenalty, false);
    }

    public static Filter playable(final LotroGame game, final int twilightModifier, final boolean ignoreRoamingPenalty, final boolean ignoreCheckingDeadPile) {
        return playable(game, 0, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile);
    }

    public static Filter playable(final LotroGame game, final int withTwilightRemoved, final int twilightModifier, final boolean ignoreRoamingPenalty, final boolean ignoreCheckingDeadPile) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                Side expectedSide = (physicalCard.getOwner().equals(gameState.getCurrentPlayerId()) ? Side.FREE_PEOPLE : Side.SHADOW);
                if (physicalCard.getBlueprint().getSide() != expectedSide)
                    return false;

                return physicalCard.getBlueprint().checkPlayRequirements(physicalCard.getOwner(), game, physicalCard, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile);
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

    private static Filter signet(final Signet signet) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.hasSignet(gameState, physicalCard, signet);
            }
        };
    }

    private static Filter race(final Race race) {
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


    private static Filter side(final Side side) {
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
                CardType.ALLY,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return physicalCard.getBlueprint().isAllyAtHome(siteNumber, siteBlock);
                    }
                });
    }

    public static final Filter allyAtHome = Filters.and(
            CardType.ALLY,
            new Filter() {
                @Override
                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                    return physicalCard.getBlueprint().isAllyAtHome(gameState.getCurrentSiteNumber(), gameState.getCurrentSiteBlock());
                }
            });

    public static final Filter currentSite = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return gameState.getCurrentSite() == physicalCard;
        }
    };

    public static Filter siteNumber(final int siteNumber) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getSiteNumber() != null) && (physicalCard.getSiteNumber() == siteNumber);
            }
        };
    }

    public static Filter siteInCurrentRegion = Filters.and(CardType.SITE,
            new Filter() {
                @Override
                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                    int siteNumber = physicalCard.getSiteNumber();
                    return GameUtils.getRegion(gameState) == GameUtils.getRegion(siteNumber);
                }
            });

    public static Filter region(final int region) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                int siteNumber = physicalCard.getSiteNumber();
                return GameUtils.getRegion(siteNumber) == region;
            }
        };
    }

    public static Filter hasAttached(final Filterable... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                List<PhysicalCard> physicalCardList = gameState.getAttachedCards(physicalCard);
                return (Filters.filter(physicalCardList, gameState, modifiersQuerying, filters).size() > 0);
            }
        };
    }

    public static Filter hasStacked(final Filterable... filter) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                List<PhysicalCard> physicalCardList = gameState.getStackedCards(physicalCard);
                return (Filters.filter(physicalCardList, gameState, modifiersQuerying, Filters.and(filter, activeSide)).size() > 0);
            }
        };
    }

    public static Filter not(final Filterable... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return !Filters.and(filters).accepts(gameState, modifiersQuerying, physicalCard);
            }
        };
    }

    public static Filter sameCard(final PhysicalCard card) {
        final int cardId = card.getCardId();
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getCardId() == cardId);
            }
        };
    }

    public static Filter in(final Collection<PhysicalCard> cards) {
        final Set<Integer> cardIds = new HashSet<Integer>();
        for (PhysicalCard card : cards)
            cardIds.add(card.getCardId());
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return cardIds.contains(physicalCard.getCardId());
            }
        };
    }

    private static Filter zone(final Zone zone) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getZone() == zone;
            }
        };
    }

    public static Filter hasWounds(final int wounds) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return gameState.getWounds(physicalCard) >= wounds;
            }
        };
    }

    public static final Filter unwounded = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return gameState.getWounds(physicalCard) == 0;
        }
    };

    public static final Filter wounded = Filters.hasWounds(1);

    public static Filter name(final String name) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return name != null && physicalCard.getBlueprint().getName() != null && physicalCard.getBlueprint().getName().equals(name);
            }
        };
    }

    private static Filter type(final CardType cardType) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getBlueprint().getCardType() == cardType)
                        || modifiersQuerying.isAdditionalCardType(gameState, physicalCard, cardType);
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

    public static Filter stackedOn(final Filterable... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getStackedOn() != null && Filters.and(filters).accepts(gameState, modifiersQuerying, physicalCard.getStackedOn());
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

    public static Filter siteControlledByAnyPlayer = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return physicalCard.getBlueprint().getCardType() == CardType.SITE && physicalCard.getCardController() != null;
        }
    };

    public static Filter siteControlled(final String playerId) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return physicalCard.getBlueprint().getCardType() == CardType.SITE && playerId.equals(physicalCard.getCardController());
            }
        };
    }

    public static Filter uncontrolledSite = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            return physicalCard.getBlueprint().getCardType() == CardType.SITE && physicalCard.getCardController() == null;
        }
    };


    private static Filter culture(final Culture culture) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getBlueprint().getCulture() == culture);
            }
        };
    }

    private static Filter keyword(final Keyword keyword) {
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
        else if (filter instanceof PhysicalCard)
            return Filters.sameCard((PhysicalCard) filter);
        else if (filter instanceof CardType)
            return _typeFilterMap.get((CardType) filter);
        else if (filter instanceof Culture)
            return _cultureFilterMap.get((Culture) filter);
        else if (filter instanceof Keyword)
            return _keywordFilterMap.get((Keyword) filter);
        else if (filter instanceof PossessionClass)
            return _possessionClassFilterMap.get((PossessionClass) filter);
        else if (filter instanceof Race)
            return _raceFilterMap.get((Race) filter);
        else if (filter instanceof Side)
            return _sideFilterMap.get((Side) filter);
        else if (filter instanceof Signet)
            return _signetFilterMap.get((Signet) filter);
        else if (filter instanceof Zone)
            return _zoneFilterMap.get((Zone) filter);
        else
            throw new IllegalArgumentException("Unknown type of filterable: " + filter);
    }

    public static Filter activeSide = new Filter() {
        @Override
        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
            boolean shadow = physicalCard.getBlueprint().getSide() == Side.SHADOW;
            if (shadow)
                return !physicalCard.getOwner().equals(gameState.getCurrentPlayerId());
            else
                return physicalCard.getOwner().equals(gameState.getCurrentPlayerId());
        }
    };

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

    public static final Filter unboundCompanion = Filters.and(CardType.COMPANION, Filters.not(Keyword.RING_BOUND));
    public static final Filter roamingMinion = Filters.and(CardType.MINION, Keyword.ROAMING);
    public static final Filter mounted = Filters.or(Filters.hasAttached(PossessionClass.MOUNT), Keyword.MOUNTED);

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

        public PhysicalCard getCard() {
            return _card;
        }
    }

    private static class SpotCountFilterCardInPlayVisitor implements PhysicalCardVisitor {
        private GameState _gameState;
        private ModifiersQuerying _modifiersQuerying;
        private Filter _filter;
        private int _spottedCount;
        private int _searchingToSpot;

        private SpotCountFilterCardInPlayVisitor(GameState gameState, ModifiersQuerying modifiersQuerying, int count, Filter filter) {
            _gameState = gameState;
            _modifiersQuerying = modifiersQuerying;
            _searchingToSpot = count;
            _filter = filter;
        }

        @Override
        public boolean visitPhysicalCard(PhysicalCard physicalCard) {
            if (_filter.accepts(_gameState, _modifiersQuerying, physicalCard)) {
                _spottedCount++;
                if (_spottedCount >= _searchingToSpot)
                    return true;
            }
            return false;
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
