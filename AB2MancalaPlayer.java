import java.util.ArrayList;

public class AB2MancalaPlayer extends MancalaPlayer implements MiniMax {

	int depthLimit;
	int totalGeneratedNodes;
	int totalStaticEvaluations;
	int childNodesExplored;

	public AB2MancalaPlayer() {
		super();
		depthLimit = 5;
		totalGeneratedNodes++;
	}

	/**
	 * 
	 * assuming we are max player
	 * 
	 * @param g
	 * @param depthLeft
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public double negaMax(GameState g, int depthLeft, double alpha, double beta) {
		ArrayList<Move> validMoves = g.getLegalMoves();

		if (validMoves.isEmpty() || depthLeft == 0) {
			totalStaticEvaluations++;
			childNodesExplored++;
			return staticEvaluator(g);
		} else {
			for (Move m : validMoves) {
				double curValue = -negaMax(g.makeMove(m), depthLeft - 1, -beta, -alpha);

				if (curValue >= beta) {
					return curValue;
				}

				if (curValue > alpha) {
					alpha = curValue;
				}
			}
			return alpha;
		}
	}

	@Override
	public Move getMove(GameState g, long deadline) {
		ArrayList<Move> validMoves = g.getLegalMoves();
		Move bestMove = null;
		double bestScore = Integer.MIN_VALUE;
		double curScore;
		int depth = depthLimit;

		for (Move m : validMoves) {
			curScore = negaMax(g.makeMove(m), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

			if (curScore > bestScore) {
				bestMove = m;
				bestScore = curScore;
			}
		}

		return bestMove;
	}

	@Override
	public double staticEvaluator(GameState state) {
		return state.currentScore();

		// return state.currentScore() - state.getPlayerScore(!state.getTurn());
//		if (state.isBottomTurn) {
//			return state.currentScore() - state.getPlayerScore(!state.getTurn());
//		} else {
//			return -(state.currentScore() - state.getPlayerScore(!state.getTurn()));
//		}
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
		// TODO Auto-generated method stub
		return (totalGeneratedNodes - 1) / (totalGeneratedNodes - childNodesExplored);
	}

	@Override
	public double getEffectiveBranchingFactor() {
		return (childNodesExplored) / (totalGeneratedNodes - childNodesExplored);
	}

	public String toString() {
		return "AlphaBeta2";
	}

}
