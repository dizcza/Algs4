import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Solver {

    private static class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private SearchNode parent;
        private int moves = 0;

        private SearchNode(Board board, SearchNode parent) {
            this.board = board;
            this.parent = parent;
            this.moves = parent.moves + 1;
        }

        private SearchNode(Board board) {
            this.board = board;
        }

        @Override
        public String toString() {
            return board.toString() + String.format("moves: %d | manhattan: %d", moves, board.manhattan());
        }

        @Override
        public int compareTo(SearchNode other) {
            int manhattanThis = this.board.manhattan();
            int manhattanOther = other.board.manhattan();
            if (manhattanThis + this.moves < manhattanOther + other.moves) {
                return -1;
            }
            else if (manhattanThis + this.moves == manhattanOther + other.moves) {
                if (manhattanThis == manhattanOther) {
                    // ==> this.moves == other.moves
                    return other.board.hamming() - this.board.hamming();
                }
                else {
                    return manhattanThis - manhattanOther;
                }
            }
            else {
                return +1;
            }
        }
    }

    private final List<Board> path;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }
        path = solve(initial);
    }

    private List<Board> solve(Board initialBoard) {
        MinPQ<SearchNode> priorityQueue = new MinPQ<>();

        SearchNode initialNode = new SearchNode(initialBoard);
        SearchNode twinNode = new SearchNode(initialBoard.twin());

        priorityQueue.insert(initialNode);
        priorityQueue.insert(twinNode);

        SearchNode deletedNode = null;
        while (!priorityQueue.isEmpty()) {
            deletedNode = priorityQueue.delMin();
            if (deletedNode.board.isGoal()) {
                break;
            }
            for (Board neighborBoard : deletedNode.board.neighbors()) {
                if (deletedNode.parent != null && neighborBoard.equals(deletedNode.parent.board)) continue;
                SearchNode childNode = new SearchNode(neighborBoard, deletedNode);
                priorityQueue.insert(childNode);
            }
        }
        assert deletedNode != null;
        List<Board> reversedPath = new ArrayList<>();
        while (deletedNode.parent != null) {
            reversedPath.add(deletedNode.board);
            deletedNode = deletedNode.parent;
        }
        Board firstBoard = deletedNode.board;
        if (firstBoard.equals(initialBoard)) {
            reversedPath.add(firstBoard);
            Collections.reverse(reversedPath);
            return reversedPath;
        }
        else {
            return null;
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return path != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) {
            return path.size() - 1;
        }
        else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? new LinkedList<>(path) : null;
    }

    private static void test(String path) {
        In in = new In(path);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            String shortName = path.substring(path.lastIndexOf('/') + 1);
            String number = shortName.substring(shortName.lastIndexOf('-') + 1, shortName.lastIndexOf('-') + 3);
            if (number.equals(String.valueOf(solver.moves()))) {
                System.out.println(shortName + " passed");
            }
            else {
                StdOut.println(shortName + ": Minimum number of moves = " + solver.moves());
//                for (Board board : solver.solution()) StdOut.println(board);
            }
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        test("/home/dizcza/IdeaProjects/Lab4_PriorityQueue/testing/puzzle01.txt");

        for (int i=0; i < 51; ++i) {
            test(String.format("/home/dizcza/IdeaProjects/Lab4_PriorityQueue/testing/puzzle%02d.txt", i));
        }

    }

}