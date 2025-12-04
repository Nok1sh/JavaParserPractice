package org.example.resolver;

import org.example.model.Player;
import org.example.model.Position;
import org.example.parser.CsvParser;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Resolver implements IResolver {

    private final List<Player> players;

    public Resolver(CsvParser parser) throws IOException {
        this.players = parser.parseCsvToList();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public int getCountWithoutAgency() {
        return (int) players.stream()
                .filter(player -> player.agency() == null || player.agency().isEmpty())
                .count();
    }

    @Override
    public int getMaxDefenderGoalsCount() {
        return players.stream()
                .filter(player -> player.position() == Position.DEFENDER)
                .mapToInt(Player::goals)
                .max()
                .orElse(0);
    }

    @Override
    public String getTheExpensiveGermanPlayerPosition() {
        return players.stream()
                .filter(player -> player.nationality().equals("Germany"))
                .max(Comparator.comparingInt(Player::transferCost))
                .map(player -> player.position().toString())
                .orElse("not players from Germany");
    }

    @Override
    public Map<String, String> getPlayersByPosition() {

        return players.stream()
                .collect(Collectors.toMap(
                        player -> player.position().toString(),
                        player -> player.toString(),
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public Set<String> getTeams() {

        return players.stream()
                .map(Player::team)
                .collect(Collectors.toSet());
    }

    @Override
    public Map<String, Integer> getTop5TeamsByGoalsCount() {

        return players.stream()
                .collect(Collectors.toMap(
                        player -> player.team(),
                        player -> player.goals(),
                        Integer::sum
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public String getAgencyWithMinPlayersCount() {

        return players.stream()
                .map(Player::agency)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()).
                limit(1).
                map(Map.Entry::getKey)
                .findFirst()
                .orElse("No agencies");
    }

    @Override
    public String getTheRudestTeam() {

        return players.stream()
                .collect(Collectors.groupingBy(
                        Player::team,
                        Collectors.averagingInt(Player::redCard)
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("not teams");
    }

    public Map<Integer, Integer> getCostDependenceOnGoals(){
        return players.stream()
                .filter(player -> player.position() == Position.FORWARD)
                .collect(Collectors.toMap(
                        Player::transferCost,
                        Player::goals
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getValue,
                        Map.Entry::getKey,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }
}
