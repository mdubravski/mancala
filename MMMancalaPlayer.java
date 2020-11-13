import java.util.ArrayList;

public class MMMancalaPlayer extends MancalaPlayer implements MiniMax {

	String name;
	private int depthLimit;
	int totalGeneratedNodes;
	int totalStaticEvaluations;
	int childNodesExplored;

	/*
	 * Makes a new MMMancalaPlayer Single argument constructor
	 * 
	 */
	public MMMancalaPlayer(String name) {
		super(name);
		depthLimit = 5;
		totalGeneratedNodes++;
	}

	/*
	 * Makes a new MMMancalaPlayer Two argument constructor
	 */
	public MMMancalaPlayer(String name, int depth) {
		super(name);
		this.depthLimit = depth;
		totalGeneratedNodes++;
	}

	/**
	 * Implementation of maxPlayer for MiniMax
	 * 
	 * @param g         current game state
	 * @param depthLeft the depth limit to search to
	 * @return score of the best move
	 */
	public double maxPlayer(GameState g, int depthLeft) {
		ArrayList<Move> validMoves = g.getLegalMoves();

		if (validMoves.isEmpty()) {
			return g.getFinalNetScore();
		} else if (depthLeft == 0) {
			totalStaticEvaluations++;
			childNodesExplored++;
			return staticEvaluator(g);
		} else {
			double best = Integer.MIN_VALUE;

			for (Move m : validMoves) {
				totalGeneratedNodes++;

				// check if move m can force an extra move
				if (g.makeMove(m).isBottomTurn) {
					best = Math.max(best, minPlayer(g.makeMove(m), depthLeft - 1));
				} else {
					best = Math.max(best, maxPlayer(g.makeMove(m), depthLeft - 1));
				}

				best = Math.max(best, minPlayer(g.makeMove(m), depthLeft - 1));
			}
			return best;
		}
	}

	/**
	 * Implementation of minPlayer for MiniMax
	 * 
	 * @param g         current game state
	 * @param depthLeft the depth limit to search to
	 * @return score of the best move
	 */
	public double minPlayer(GameState g, int depthLeft) {
		ArrayList<Move> validMoves = g.getLegalMoves();

		if (validMoves.isEmpty()) {
			return g.getFinalNetScore();
		} else if (depthLeft == 0) {
			totalStaticEvaluations++;
			childNodesExplored++;
			return staticEvaluator(g);
		} else {
			double best = Integer.MAX_VALUE;
			for (Move m : validMoves) {
				totalGeneratedNodes++;

				// check if move m can force an extra move
				if (g.makeMove(m).isBottomTurn) {
					best = Math.min(best, maxPlayer(g.makeMove(m), depthLeft - 1));
				} else {
					best = Math.min(best, minPlayer(g.makeMove(m), depthLeft - 1));
				}

				best = Math.min(best, maxPlayer(g.makeMove(m), depthLeft - 1));
			}
			return best;
		}
	}

	@Override
	/**
	 * 
	 * @g the GameState to make a move
	 * @deadline the system time when it must return by
	 * @return the move the player wants to make
	 */
	public Move getMove(GameState g, long deadline) {
		ArrayList<Move> validMoves = g.getLegalMoves();
		Move bestMove = null;
		double bestScore;
		double curScore;
		int depth = depthLimit;

		if (g.isBottomTurn) {
			bestScore = Integer.MIN_VALUE;
		} else {
			bestScore = Integer.MAX_VALUE;
		}

		for (Move m : validMoves) {
			GameState childState = g.makeMove(m);
			totalGeneratedNodes++;

			if (childState.isBottomTurn) {
				curScore = maxPlayer(g.makeMove(m), depth - 1);
			} else {
				curScore = minPlayer(g.makeMove(m), depth - 1);
			}

			if (g.isBottomTurn) {
				if (curScore > bestScore) {
					bestMove = m;
					bestScore = curScore;
				} else {
					continue;
				}
			} else {
				if (curScore < bestScore) {
					bestMove = m;
					bestScore = curScore;
				} else {
					continue;
				}
			}
		}
		return bestMove;
	}

	@Override
	/**
	 * The static evaluation function for your search.
	 * 
	 * @param state the state to be evaluated.
	 * @return an integer score for the value of the state to the max player.
	 */
	public double staticEvaluator(GameState state) {
		return state.currentScore();
	}

	@Override
	/**
	 * Get the number of nodes that were generated during the search.
	 * 
	 * @return the number of nodes generated.
	 */
	public int getNodesGenerated() {
		return totalGeneratedNodes;
	}

	@Override
	/**
	 * Get the number of static evaluations that were performed during the search.
	 * 
	 * @return the number of static evaluations performed.
	 */
	public int getStaticEvaluations() {
		return totalStaticEvaluations;
	}

	@Override
	/**
	 * Get the average branching factor of the nodes that were expanded during the
	 * search. This is to be computed based upon the actual number of children for
	 * each node.
	 * 
	 * 
	 * 
	 * Avg by the whole tree should be like 5
	 * 
	 * @return the average branching factor.
	 */
	public double getAveBranchingFactor() {
		return (totalGeneratedNodes - 1) / (totalGeneratedNodes - childNodesExplored);
	}

	@Override
	/**
	 * Get the effective branching factor of the nodes that were expanded during the
	 * search. This is to be computed based upon the number of children that are
	 * explored in the search. Without alpha/beta pruning this number will be equal
	 * to the average branching factor. With alpha/beta pruning it should be
	 * significantly smaller.
	 * 
	 * @return the effective branching factor.
	 */
	public double getEffectiveBranchingFactor() {
		// TODO Auto-generated method stub
		return (childNodesExplored) / (totalGeneratedNodes - childNodesExplored);
	}

	/**
	 * returns the name of MancalaPlayer
	 */
	public String toString() {
		return "MiniMax";
	}

}
