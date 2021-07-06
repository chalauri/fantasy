package ge.chalauri.fantasy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ge.chalauri.fantasy.common.Position;
import ge.chalauri.fantasy.model.Country;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;

import static ge.chalauri.fantasy.common.Position.ATTACKER;
import static ge.chalauri.fantasy.common.Position.DEFENDER;
import static ge.chalauri.fantasy.common.Position.GOALKEEPER;
import static ge.chalauri.fantasy.common.Position.MIDFIELDER;
import static ge.chalauri.fantasy.utils.APIConstants.NUMBER_OF_ATTACKERS;
import static ge.chalauri.fantasy.utils.APIConstants.NUMBER_OF_DEFENDERS;
import static ge.chalauri.fantasy.utils.APIConstants.NUMBER_OF_GOALKEEPERS;
import static ge.chalauri.fantasy.utils.APIConstants.NUMBER_OF_MIDFIELDERS;
import static ge.chalauri.fantasy.utils.APIConstants.PLAYER_INITIAL_VALUE;
import static ge.chalauri.fantasy.utils.APIConstants.TEAM_INITIAL_BUDGET;
import static ge.chalauri.fantasy.utils.APIConstants.TEAM_INITIAL_VALUE;

public class TeamGenerator {

    private static final Random RANDOM = new Random();

    public static Team generateTeam(List<Country> countries) {

        List<Player> players = new ArrayList<>();
        int randomIndex = new Random().nextInt(countries.size());

        generatePlayers(countries, players, NUMBER_OF_GOALKEEPERS, GOALKEEPER);

        generatePlayers(countries, players, NUMBER_OF_DEFENDERS, DEFENDER);

        generatePlayers(countries, players, NUMBER_OF_MIDFIELDERS, MIDFIELDER);

        generatePlayers(countries, players, NUMBER_OF_ATTACKERS, ATTACKER);

        return Team
            .builder()
            .country(countries.get(randomIndex))
            .teamValue(TEAM_INITIAL_VALUE)
            .players(players)
            .name("Team " + System.currentTimeMillis())
            .budget(TEAM_INITIAL_BUDGET)
            .build();
    }

    private static void generatePlayers(List<Country> countries, List<Player> players, int numberOfPlayers, Position position) {
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(generatePlayer(position.getLabel(), countries));
        }
    }

    private static Player generatePlayer(String position, List<Country> countries) {
        int age = RANDOM.nextInt(23) + 18;
        int randomIndex = new Random().nextInt(countries.size());

        return Player
            .builder()
            .age(age)
            .firstName("Name " + RANDOM.nextInt(100))
            .lastName("Surname " + RANDOM.nextInt(100))
            .price(PLAYER_INITIAL_VALUE)
            .country(countries.get(randomIndex))
            .position(position)
            .build();
    }

}
