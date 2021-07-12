/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * 1. What is the order of growth of the amount of memory (in the worst case) that your program
 * uses to determine whether one team is eliminated? In particular, how many vertices and edges are
 * in the flow network as a function of the number of teams n?
 * <p>
 * V = N * (N - 1) / 2 + N + 2
 * E = 3 * N * (N - 1) / 2 + N
 * <p>
 * 2. What is the order of growth of the running time (in the worst case) of your program to
 * determine whether one team is eliminated as a function of the number of teams n? In your
 * calculation, assume that the order of growth of the running time (in the worst case) to compute
 * a maxflow in a network with V vertices and E edges is V E2.
 * <p>
 * Time cmoplexity in O(VE^2) ~ O(N^6)
 * <p>
 * 3.Consider the sports division defined in teams12.txt. Explain in nontechnical terms (using the
 * results of certificate of elimination and grade-school arithmetic) why Japan is mathematically
 * eliminated.
 * <p>
 * 日本最佳成績:6勝5敗
 * 考慮 subset R = { Iran Brazil Russia Poland }
 * 戰績分別是 {5勝2敗, 5勝1敗, 5勝1敗, 6勝1敗}
 * 由它們對戰組合可以知道, 這四隊平均每隊還有一勝, 不管是誰拿到勝利, 日本一定會被淘汰
 */
public class BaseballElimination {

    private static final int NO = 0;
    private static final int WINS = 1;
    private static final int LOSSES = 2;
    private static final int REMAINING = 3;
    private TreeMap<String, Integer[]> recordMap; // team -> team紀錄 (由隊名取得隊伍的紀錄)
    private String[] teamList;  // index -> team mapping (由index取得隊名)
    private int[][] matches;    // 剩餘對戰組合
    private Bag<String> subset; // 考慮這些隊伍就可證明目標隊伍已經淘汰

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("argumnet for BaseballElimination is null");

        try {
            In in = new In(filename);
            int numOfTeams = in.readInt();
            matches = new int[numOfTeams][numOfTeams];
            teamList = new String[numOfTeams];
            recordMap = new TreeMap<>();
            for (int i = 0; i < numOfTeams; i++) {
                String team = in.readString();

                // init index -> team mapping
                teamList[i] = team;

                // init team record
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
            throw new IllegalArgumentException(
                    "File: " + filename + "contains invalid constructor");
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
                if (otherRecord[WINS] > teamRecord[WINS] + teamRecord[REMAINING]) {
                    subset = new Bag<>();
                    subset.add(otherTeam);
                    return true;
                }
            }
        }

        // run flow network max flow problem to check nontrivial elimination
        return getEliminatedByNetwork(team);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (isEliminated(team)) return subset;
        else return null;
    }

    // 使用Flow Network來算出該隊伍是否被淘汰
    private boolean getEliminatedByNetwork(String team) {
        int n = numberOfTeams(); // 將team也加入 FlowNetwork的vertices中, 但不加入edge

        // 0 ~ n - 1為team vertices, s為V - 2, t為V - 1, 其他為game vertices
        int V = n * (n - 1) / 2 + n + 2;
        int s = V - 2;
        int t = V - 1;
        FlowNetwork G = new FlowNetwork(V);
        // 連接team vertices與sink, capacity為目標隊伍的win - remaining + 別隊的勝場數
        Integer[] teamRecord = getRecord(team);
        int teamNo = teamRecord[NO];
        int wins = teamRecord[WINS];
        int remaining = teamRecord[REMAINING];
        for (int i = 0; i < n; i++) {
            if (i == teamNo) continue;
            String otherTeam = teamList[i];
            int otherTeamWins = getRecord(otherTeam)[WINS];
            G.addEdge(new FlowEdge(i, t, wins + remaining - otherTeamWins));
        }

        // 連接source與game vertices, game vertices與team vertices
        int gameVertex = n;
        int max = 0;
        for (int i = 0; i < matches.length; i++) {
            if (i == teamNo) continue;
            for (int j = i + 1; j < matches[i].length; j++) {
                if (j == teamNo) continue;
                G.addEdge(new FlowEdge(s, gameVertex,
                                       matches[i][j])); // s -> game vertices with capacity 為對戰場數
                G.addEdge(new FlowEdge(gameVertex, i,
                                       Integer.MAX_VALUE)); // game vertices -> 其中一個team vertices
                G.addEdge(new FlowEdge(gameVertex, j,
                                       Integer.MAX_VALUE)); // game vertices -> 另一個team vertices
                gameVertex++;
                max += matches[i][j];
            }
        }

        FordFulkerson maxFlow = new FordFulkerson(G, s, t);
        // 其他隊伍的剩餘比賽數與算出來的maxFlow相同
        // -> 代表s流出去的flow滿了, 但sink端接收的還沒滿
        // -> 表示該隊伍還有空間可以取得較高的勝差, 所以還沒有被淘汰
        if (max - (int) maxFlow.value() == 0) {
            return false;
        }
        // 其他就代表, s流出去沒滿, 但是連接sink端的edge滿了
        // -> 代表該退伍已經被淘汰
        // (因為連接sink的capacity表示其他退伍與該隊伍的勝差, 因為s流出還沒滿, 所以s可再流出至少一場勝利
        // 但是這場勝利給任何一個隊伍都表示目標隊伍被淘汰, 故目標隊伍已經淘汰)
        else {
            subset = new Bag<>();
            for (int i = 0; i < n; i++) {
                if (i == teamNo) continue;
                if (maxFlow.inCut(i)) subset.add(teamList[i]);
            }
            return true;
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
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
