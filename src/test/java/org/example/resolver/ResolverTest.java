package org.example.resolver;

import org.example.model.Player;
import org.example.model.Position;
import org.example.parser.CsvParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResolverTest {

    private static final CsvParser mockParser = mock(CsvParser.class);

    @Test
    public void testOneDefender() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(new Player("Иванов Иван", "", Position.DEFENDER, "", "", 0, 0, 10, 0, 0, 0)));

        var resolver = new Resolver(mockParser);

        assertEquals(10, resolver.getMaxDefenderGoalsCount());

    }

    @Test
    public void testManyDefender() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(new Player("Иванов Иван", "", Position.DEFENDER, "", "", 0, 0, 10, 0, 0, 0),
                        new Player("Иванов Александр", "", Position.DEFENDER, "", "", 0, 0, 100, 0, 0, 0)));

        var resolver = new Resolver(mockParser);

        assertEquals(100, resolver.getMaxDefenderGoalsCount());

    }

    @Test
    public void testNoPlayers() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of());

        var resolver = new Resolver(mockParser);

        assertEquals(0, resolver.getMaxDefenderGoalsCount());

    }

    @Test
    public void testNoDefender() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(new Player("Иванов Иван", "", Position.FORWARD, "", "", 0, 0, 10, 0, 0, 0)));

        var resolver = new Resolver(mockParser);

        assertEquals(0, resolver.getMaxDefenderGoalsCount());

    }

    @Test
    public void testGetCountWithoutAgency() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "", Position.FORWARD, "", "", 0, 0, 10, 0, 0, 0),
                        new Player("Петров Петр", "", Position.MIDFIELD, "Agenta", "", 0, 0, 5, 0, 0, 0),
                        new Player("Сидоров Сидор", "", Position.DEFENDER, "", "", 0, 0, 3, 0, 0, 0),
                        new Player("Козлов Козел", "", Position.MIDFIELD, null, "123", 0, 0, 7, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);

        assertEquals(3, resolver.getCountWithoutAgency());
    }

    @Test
    public void testGetTheExpensiveGermanPlayerPosition() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Müller", "", Position.MIDFIELD, "Germany", "", 50, 0, 10, 0, 0, 0),
                        new Player("Schmidt", "", Position.FORWARD, "Germany", "", 100, 0, 15, 0, 0, 0),
                        new Player("Fischer", "", Position.DEFENDER, "Germany", "", 75, 0, 5, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);

        assertEquals("FORWARD", resolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    public void testGetTheExpensiveGermanPlayerPositionNoGerman() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "", Position.FORWARD, "Russia", "", 0, 0, 10, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);

        assertEquals("not players from Germany", resolver.getTheExpensiveGermanPlayerPosition());
    }

    @Test
    public void testGetPlayersByPosition() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "Team1", Position.FORWARD, "", "", 0, 0, 10, 0, 0, 0),
                        new Player("Петров Петр", "Team2", Position.MIDFIELD, "", "", 0, 0, 5, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);
        var result = resolver.getPlayersByPosition();

        assertEquals(2, result.size());
        assertTrue(result.containsKey("FORWARD"));
        assertTrue(result.containsKey("MIDFIELD"));
        assertTrue(result.get("FORWARD").contains("Иванов Иван"));
    }

    @Test
    public void testGetTeams() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "Team1", Position.FORWARD, "", "", 0, 0, 10, 0, 0, 0),
                        new Player("Петров Петр", "Team2", Position.MIDFIELD, "", "", 0, 0, 5, 0, 0, 0),
                        new Player("Сидоров Сидор", "Team1", Position.DEFENDER, "", "", 0, 0, 3, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);
        var result = resolver.getTeams();

        assertEquals(2, result.size());
        assertTrue(result.contains("Team1"));
        assertTrue(result.contains("Team2"));
    }

    @Test
    public void testGetTop5TeamsByGoalsCount() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "Team1", Position.FORWARD, "", "", 0, 0, 10, 0, 0, 0),
                        new Player("Петров Петр", "Team2", Position.MIDFIELD, "", "", 0, 0, 15, 0, 0, 0),
                        new Player("Сидоров Сидор", "Team1", Position.DEFENDER, "", "", 0, 0, 5, 0, 0, 0),
                        new Player("Козлов Козел", "Team3", Position.FORWARD, "", "", 0, 0, 20, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);
        var result = resolver.getTop5TeamsByGoalsCount();

        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(20), result.get("Team3"));
        assertEquals(Integer.valueOf(15), result.get("Team2"));
        assertEquals(Integer.valueOf(15), result.get("Team1"));
    }

    @Test
    public void testGetAgencyWithMinPlayersCount() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "", Position.FORWARD, "", "AgencyA", 0, 0, 10, 0, 0, 0),
                        new Player("Петров Петр", "", Position.MIDFIELD, "", "AgencyA", 0, 0, 5, 0, 0, 0),
                        new Player("Сидоров Сидор", "", Position.DEFENDER, "", "AgencyB", 0, 0, 3, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);

        assertEquals("AgencyB", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    public void testGetAgencyWithMinPlayersCountNoAgencies() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of());

        var resolver = new Resolver(mockParser);

        assertEquals("No agencies", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    public void testGetTheRudestTeam() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "Team1", Position.FORWARD, "", "", 0, 0, 10, 0, 1, 1),
                        new Player("Петров Петр", "Team1", Position.MIDFIELD, "", "", 0, 0, 5, 0, 2, 2),
                        new Player("Сидоров Сидор", "Team2", Position.DEFENDER, "", "", 0, 0, 3, 0, 1, 0)
                ));

        var resolver = new Resolver(mockParser);

        assertEquals("Team1", resolver.getTheRudestTeam());
    }

    @Test
    public void testGetTheRudestTeamNoTeams() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of());

        var resolver = new Resolver(mockParser);

        assertEquals("not teams", resolver.getTheRudestTeam());
    }

    @Test
    public void testGetCostDependenceOnGoals() throws IOException {
        // Создаем тестовые данные с нападающими
        var forward1 = new Player("Forward1", "", Position.FORWARD, "", "", 100, 0, 5, 0, 0, 0);
        var forward2 = new Player("Forward2", "", Position.FORWARD, "", "", 200, 0, 10, 0, 0, 0);
        var forward3 = new Player("Forward3", "", Position.FORWARD, "", "", 150, 0, 7, 0, 0, 0);
        var defender = new Player("Defender", "", Position.DEFENDER, "", "", 80, 0, 3, 0, 0, 0);

        // Создаем мок-парсер
        var mockParser = mock(CsvParser.class);
        try {
            when(mockParser.parseCsvToList()).thenReturn(List.of(forward1, forward2, forward3, defender));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Создаем резолвер
        var resolver = new Resolver(mockParser);

        // Вызываем тестируемый метод
        var result = resolver.getCostDependenceOnGoals();

        // Проверяем результат
        assertEquals(3, result.size());
        
        // Проверяем, что результат отсортирован по количеству голов (ключам)
        var goalsList = List.copyOf(result.keySet());
        assertEquals(List.of(5, 7, 10), goalsList);
        
        // Проверяем соответствие стоимости и голов для каждого игрока
        assertEquals(Integer.valueOf(100), result.get(5));
        assertEquals(Integer.valueOf(150), result.get(7));
        assertEquals(Integer.valueOf(200), result.get(10));
        
        // Проверяем, что защитник не включен в результат
        assertFalse(result.containsValue(80));
    }

    @Test
    public void testGetCostDependenceOnGoalsNoForwards() throws IOException {
        // Тест для случая, когда нет нападающих
        var defender = new Player("Defender", "", Position.DEFENDER, "", "", 80, 0, 3, 0, 0, 0);
        var midfielder = new Player("Midfielder", "", Position.MIDFIELD, "", "", 90, 0, 2, 0, 0, 0);

        var mockParser = mock(CsvParser.class);
        try {
            when(mockParser.parseCsvToList()).thenReturn(List.of(defender, midfielder));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        var resolver = new Resolver(mockParser);
        var result = resolver.getCostDependenceOnGoals();

        // Должен вернуть пустой результат
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCostDependenceOnGoalsSameGoalsDifferentCost() throws IOException {
        // Тест для случая, когда у игроков одинаковое количество голов, но разная стоимость
        var forward1 = new Player("Forward1", "", Position.FORWARD, "", "", 100, 0, 5, 0, 0, 0);
        var forward2 = new Player("Forward2", "", Position.FORWARD, "", "", 200, 0, 5, 0, 0, 0);

        var mockParser = mock(CsvParser.class);
        try {
            when(mockParser.parseCsvToList()).thenReturn(List.of(forward1, forward2));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        var resolver = new Resolver(mockParser);
        var result = resolver.getCostDependenceOnGoals();

        // При одинаковом количестве голов, в мапе будет только одна запись
        // Второй игрок перезапишет первого из-за коллектора
        assertEquals(1, result.size(), "При одинаковом количестве голов должен быть только один результат");
        assertEquals(Integer.valueOf(100), result.get(5), "При конфликте ключей должен остаться первый игрок");
    }

    @Test
    public void testGetPlayers() throws IOException {
        // Подготавливаем тестовые данные
        var player1 = new Player("Иванов Иван", "Team1", Position.FORWARD, "Russia", "", 100, 10, 5, 0, 0, 0);
        var player2 = new Player("Петров Петр", "Team2", Position.MIDFIELD, "Russia", "AgencyA", 150, 15, 8, 0, 0, 0);
        var player3 = new Player("Сидоров Сидор", "Team1", Position.DEFENDER, "Russia", "", 80, 8, 2, 0, 0, 0);

        // Настраиваем мок
        when(mockParser.parseCsvToList()).thenReturn(List.of(player1, player2, player3));

        // Создаем резолвер
        var resolver = new Resolver(mockParser);

        // Вызываем тестируемый метод
        var result = resolver.getPlayers();

        // Проверяем результат
        assertEquals(3, result.size());
        assertTrue(result.contains(player1));
        assertTrue(result.contains(player2));
        assertTrue(result.contains(player3));
        assertSame(resolver.getPlayers(), resolver.getPlayers(), "Метод должен возвращать один и тот же объект при каждом вызове");
    }

    @Test
    public void testConstructorIOException() {
        var failingParser = mock(CsvParser.class);
        try {
            when(failingParser.parseCsvToList()).thenThrow(new IOException("File not found"));
        } catch (IOException e) {
            fail("Failed to configure mock to throw exception");
        }

        assertThrows(IOException.class, () -> {
            new Resolver(failingParser);
        });
    }

    @Test
    public void testGetPlayersByPositionEmptyList() throws IOException {
        when(mockParser.parseCsvToList()).thenReturn(List.of());

        var resolver = new Resolver(mockParser);
        var result = resolver.getPlayersByPosition();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTeamsEmptyList() throws IOException {
        when(mockParser.parseCsvToList()).thenReturn(List.of());

        var resolver = new Resolver(mockParser);
        var result = resolver.getTeams();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTop5TeamsByGoalsCountEmptyList() throws IOException {
        when(mockParser.parseCsvToList()).thenReturn(List.of());

        var resolver = new Resolver(mockParser);
        var result = resolver.getTop5TeamsByGoalsCount();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTop5TeamsByGoalsCountWithSameTeam() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "Team1", Position.FORWARD, "", "", 0, 0, 10, 0, 0, 0),
                        new Player("Петров Петр", "Team1", Position.MIDFIELD, "", "", 0, 0, 15, 0, 0, 0),
                        new Player("Сидоров Сидор", "Team2", Position.DEFENDER, "", "", 0, 0, 5, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);
        var result = resolver.getTop5TeamsByGoalsCount();

        assertEquals(2, result.size());
        assertTrue(result.containsKey("Team1"));
        assertTrue(result.containsKey("Team2"));
        assertEquals(Integer.valueOf(25), result.get("Team1"));
        assertTrue(result.containsKey("Team2"));
        assertEquals(Integer.valueOf(5), result.get("Team2"));
        assertEquals(Integer.valueOf(5), result.get("Team2"));
    }

    @Test
    public void testGetAgencyWithMinPlayersCountEmptyList() throws IOException {
        when(mockParser.parseCsvToList()).thenReturn(List.of());

        var resolver = new Resolver(mockParser);

        assertEquals("No agencies", resolver.getAgencyWithMinPlayersCount());
    }

    @Test
    public void testGetTheRudestTeamEmptyList() throws IOException {
        when(mockParser.parseCsvToList()).thenReturn(List.of());

        var resolver = new Resolver(mockParser);

        assertEquals("not teams", resolver.getTheRudestTeam());
    }

    @Test
    public void testGetCostDependenceOnGoalsEmptyList() throws IOException {
        when(mockParser.parseCsvToList()).thenReturn(List.of());

        var resolver = new Resolver(mockParser);
        var result = resolver.getCostDependenceOnGoals();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPlayersByPositionWithSamePosition() throws IOException {
        when(mockParser.parseCsvToList())
                .thenReturn(List.of(
                        new Player("Иванов Иван", "Team1", Position.FORWARD, "", "", 0, 0, 10, 0, 0, 0),
                        new Player("Петров Петр", "Team2", Position.FORWARD, "", "", 0, 0, 5, 0, 0, 0)
                ));

        var resolver = new Resolver(mockParser);
        var result = resolver.getPlayersByPosition();

        assertEquals(1, result.size());
        assertTrue(result.containsKey("FORWARD"));
        assertTrue(result.get("FORWARD").contains("Иванов Иван"));
        assertFalse(result.get("FORWARD").contains("Петров Петр"));
    }
}