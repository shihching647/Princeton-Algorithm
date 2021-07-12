/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;

import java.util.NoSuchElementException;
import java.util.TreeMap;

public class BaseballElimination {

    private static final int NO = 0;
    private static final int WINS = 1;
    private static final int LOSSES = 2;
    private static final int REMAINING = 3;
    private TreeMap<String, Integer[]> recordMap;
    private int[][] matches;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("argumnet for BaseballElimination is null");

        try {
            In in = new In(filename);
            int numOfTeams = in.readInt();
            matches = new int[numOfTeams][numOfTeams];
            recordMap = new TreeMap<>();
            for (int i = 0; i < numOfTeams; i++) {
                // init team record
                String team = in.readString();
                int wins = in.readInt();
                int losses = in.readInt();
                int remaining = in.readInt();
                recordMap.put(team, new Integer[] { i, wins, losses, remaining });

                // init team matches
                for (int j = 0; j < numOfTeams; j++) {
                    matches[i][j] = in.readInt();
                }
            }
        }
        catch (NoSuchElementException e) {
            System.err.println("File: " + filename + "contains invalid constructor");
        }
    }

    // number of teams
    public int numberOfTeams() {
        return recordMap.size();
    }

    // all teams
    public Iterable<String> teams() {
        return recordMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        Integer[] teamRecord = getRecord(team);
        return teamRecord[WINS];
    }

    // number of losses for given team
    public int losses(String team) {
        Integer[] teamRecord = getRecord(team);
        return teamRecord[LOSSES];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        Integer[] teamRecord = getRecord(team);
        return teamRecord[REMAINING];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        Integer[] teamRecord1 = getRecord(team1);
        Integer[] teamRecord2 = getRecord(team2);
        return matches[teamRecord1[NO]][teamRecord2[NO]];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        Integer[] teamRecord = getRecord(team);

        // check trivial elimination(其他隊的勝場數已經超過此隊的勝場數+剩餘比賽)
        for (String otherTeam : recordMap.keySet()) {
            if (!team.equals(otherTeam)) {
                Integer[] otherRecord = getRecord(otherTeam);
                if (otherRecord[WINS] > teamRecord[WINS] + teamRecord[REMAINING])
                    return true;
            }
        }

        // run flow network max flow problem to check nontrivial elimination
        return getEliminatedByNetwork(team);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

    }

    private boolean getEliminatedByNetwork(String team) {
        int V = (numberOfTeams() - 1) * (numberOfTeams() - 2) / 2 + numberOfTeams() + 2;
        int s = V - 2;
        int t = V - 1;
        FlowNetwork G = new FlowNetwork(V);
        for (String otherTeam : recordMap.keySet()) {
            if (!team.equals(otherTeam)) {
                
            }
        }

    }

    private Integer[] getRecord(String team) {
        if (team == null)
            throw new IllegalArgumentException("team is null");
        Integer[] teamRecord = recordMap.get(team);
        if (teamRecord == null)
            throw new IllegalArgumentException("team is not in record");
        return teamRecord;
    }

    public static void main(String[] args) {

    }
}
