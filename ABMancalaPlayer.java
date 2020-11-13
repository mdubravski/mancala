import java.util.ArrayList;

public class ABMancalaPlayer extends MancalaPlayer implements MiniMax {

	int depthLimit;
	int totalGeneratedNodes;
	int totalStaticEvaluations;
	int childNodesExplored;

	/*
	 * 
	 * Makes a new ABMancalaPlayer
	 */
	public ABMancalaPlayer() {
		super();
		depthLimit = 5;
		totalGeneratedNodes++;
	}

	/*
	 * Single argument constructor to make new ABMancalaPlayer
	 * 
	 */
	public ABMancalaPlayer(int depth) {
		super();
		this.depthLimit = depth;
		totalGeneratedNodes++;
	}

	/**
	 * Implementation of maxPlayer for alpha-beta
	 * 
	 * @param g         current game state
	 * @param depthLeft the depth limit to search to
	 * @return score of the best move
	 */
	public double maxPlayer(GameState g, int depthLeft, double alpha, double beta) {
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
					best = Math.max(best, minPlayer(g.makeMove(m), depthLeft - 1, alpha, beta));
				} else {
					best = Math.max(best, maxPlayer(g.makeMove(m), depthLeft - 1, alpha, beta));
				}

				best = Math.max(best, minPlayer(g.makeMove(m), depthLeft - 1, alpha, beta));

				if (best >= beta) {
					return best;
				}
				alpha = Math.max(alpha, best);
			}
			return best;
		}
	}

	/**
	 * Implementation of minPlayer for alpha-beta
	 * 
	 * @param g         current game state
	 * @param depthLeft the depth limit to search to
	 * @return score of the best move
	 */
	public double minPlayer(GameState g, int depthLeft, double alpha, double beta) {
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
					best = Math.min(best, maxPlayer(g.makeMove(m), depthLeft - 1, alpha, beta));
				} else {
					best = Math.min(best, minPlayer(g.makeMove(m), depthLeft - 1, alpha, beta));
				}

				best = Math.min(best, maxPlayer(g.makeMove(m), depthLeft - 1, alpha, beta));

				if (best <= alpha) {
					return best;
				}
				beta = Math.min(beta, best);
			}
			return best;
		}
	}

	@Override
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

			totalGeneratedNodes++;

			if (g.makeMove(m).isBottomTurn) {
				curScore = maxPlayer(g.makeMove(m), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
			} else {
				curScore = minPlayer(g.makeMove(m), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
	public double staticEvaluator(GameState state) {
		return state.currentScore();
	}

	@Override
	public int getNodesGenerated() {
		return totalGeneratedNodes;
	}

	@Override
	public int getStaticEvaluations() {
		return totalStaticEvaluations;
	}

	@Override
	public double getAveBranchingFactor() {
		return (totalGeneratedNodes - 1) / (totalGeneratedNodes - childNodesExplored);
	}

	@Override
	public double getEffectiveBranchingFactor() {
		return (childNodesExplored) / (totalGeneratedNodes - childNodesExplored);
	}

	public String toString() {
		return "AlphaBeta";
	}
}
