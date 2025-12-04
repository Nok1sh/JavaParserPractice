package org.example;

import org.example.graph.LineChart;
import org.example.model.TranslatorPosition;
import org.example.parser.CsvParser;
import org.example.resolver.Resolver;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        var players = new CsvParser("fakePlayers.csv");
        Resolver resolver = new Resolver(players);
        // task 1
        System.out.println("Task 1");

        System.out.println("Количество игроков, интересы которых не " +
                "представляет агентство: " + resolver.getCountWithoutAgency());
        System.out.println();
        System.out.println("Максимальное число голов, забитых защитником: " +
                resolver.getMaxDefenderGoalsCount());
        System.out.println();
        TranslatorPosition translatorPos = new TranslatorPosition();

        String richestPosition = resolver.getTheExpensiveGermanPlayerPosition();
        System.out.println("Позиция самого дорогого немецкого игрока: " + translatorPos.translatePosition(richestPosition));
        System.out.println();
        System.out.println("имена игроков, сгруппированные по позициям: " + resolver.getPlayersByPosition());
        System.out.println();
        System.out.println("Команды: " + resolver.getTeams());
        System.out.println();
        System.out.println("Топ-5 команд по количеству забитых мячей: " + resolver.getTop5TeamsByGoalsCount());
        System.out.println();
        System.out.println("Агентство, количество игроков которого наименьшая: " + resolver.getAgencyWithMinPlayersCount());
        System.out.println();
        System.out.println("Команда с наибольшим средним числом красных карточек на одного игрока: " +
                resolver.getTheRudestTeam());
        System.out.println();
        System.out.println("Task 2");
        // task 2
        Map<Integer, Integer> data = resolver.getCostDependenceOnGoals();
        SwingUtilities.invokeLater(() -> {
            LineChart example = new LineChart("Пример линейного графика", data);
            example.setSize(800, 600);
            example.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}

