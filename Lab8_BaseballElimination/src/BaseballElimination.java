
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class BaseballElimination {

    private final Map<String, Integer> teamsMap;
    private final String[] teamNames;
    private final int N;
    private int maxWins = 0;
    private int teamWithMaxWins;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] gamesLeft;

    private static class Answer {
        private final boolean eliminated;
        private final Iterable<String> R;

        private Answer(boolean eliminated, Iterable<String> R) {
            this.eliminated = eliminated;
            this.R = R;
        }
    }

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        N = scanner.nextInt();
        scanner.skip("\n");
        teamsMap = new HashMap<>(N);
        teamNames = new String[N];
        w = new int[N];
        l = new int[N];
        r = new int[N];
        gamesLeft = new int[N][N];
        for (int i = 0; i < N; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" +");
            teamNames[i] = parts[0];
            teamsMap.put(teamNames[i], i);
            w[i] = Integer.parseInt(parts[1]);
            l[i] = Integer.parseInt(parts[2]);
            r[i] = Integer.parseInt(parts[3]);
            if (maxWins < w[i]) {
                teamWithMaxWins = i;
                maxWins = w[i];
            }
            for (int j = 0; j < N; ++j) {
                gamesLeft[i][j] = Integer.parseInt(parts[j + 4]);
            }
        }
    }

    // number of teamsMap
    public int numberOfTeams() {
        return N;
    }

    // all teamsMap
    public Iterable<String> teams() {
        return teamsMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        Objects.requireNonNull(team);
        return w[teamsMap.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        Objects.requireNonNull(team);
        return l[teamsMap.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        Objects.requireNonNull(team);
        return r[teamsMap.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        Objects.requireNonNull(team1);
        Objects.requireNonNull(team2);
        return gamesLeft[teamsMap.get(team1)][teamsMap.get(team2)];
    }

    private boolean isTrivial(int team) {
        return w[team] + r[team] < maxWins;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        Objects.requireNonNull(team);
        int teamId = teamsMap.get(team);
        if (isTrivial(teamId)) {
            return true;
        }
        else  {
            return buildNetwork(teamId).eliminated;
        }
    }

    // subset R of teamsMap that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        Objects.requireNonNull(team);
        int teamId = teamsMap.get(team);
        if (isTrivial(teamId)) {
            return Collections.singletonList(teamNames[teamWithMaxWins]);
        }
        else {
            return buildNetwork(teamId).R;
        }
    }

    private Answer buildNetwork(int team) {
        final int games = (N - 1) * (N - 2) / 2;
        final int V = games + N + 1;
        final int sink = V - 1;
        final int wr = w[team] + r[team];
        int sourceCapacity = 0;
        FlowNetwork network = new FlowNetwork(V);
        for (int i = 0, gid = 1; i < N - 1; ++i) {
            if (i != team) {
                for (int j = i + 1; j < N; ++j) {
                    if (j != team) {
                        int firstTeam = 1 + games + i;
                        int secondTeam = 1 + games + j;
                        sourceCapacity += gamesLeft[i][j];
                        FlowEdge sourceToGame = new FlowEdge(0, gid, gamesLeft[i][j]);
                        FlowEdge toFirstTeam = new FlowEdge(gid, firstTeam, Double.POSITIVE_INFINITY);
                        FlowEdge toSecondTeam = new FlowEdge(gid, secondTeam, Double.POSITIVE_INFINITY);
                        FlowEdge firstToSink = new FlowEdge(firstTeam, sink, wr - w[i]);
                        FlowEdge secondToSink = new FlowEdge(secondTeam, sink, wr - w[j]);
                        network.addEdge(sourceToGame);
                        network.addEdge(toFirstTeam);
                        network.addEdge(toSecondTeam);
                        network.addEdge(firstToSink);
                        network.addEdge(secondToSink);
                        gid++;
                    }
                }
            }
        }
        FordFulkerson ff = new FordFulkerson(network, 0, sink);
        if (ff.value() < sourceCapacity) {
            List<String> reasons = new ArrayList<>();
            for (int i = 0; i < N; ++i) {
                if (i != team && ff.inCut(sink - N + 1)) {
                    reasons.add(teamNames[i]);
                }
            }
            return new Answer(true, reasons);
        }
        else {
            return new Answer(false, null);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        BaseballElimination division = new BaseballElimination("Lab8_BaseballElimination/testing/teams5.txt");
        for (String team : division.teams()) {
            if (!team.equals("Detroit")) continue;
            if (division.isEliminated(team)) {
                int tid = division.teamsMap.get(team);
                if (division.w[tid] + division.r[tid] < division.maxWins) {
                    StdOut.print("[TRIVIAL] ");
                }
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
