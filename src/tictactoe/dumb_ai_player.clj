(ns tictactoe.dumb-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [move-legal]]
        [tictactoe.ui-handler]))

(defrecord DumbAiPlayer [calls moves signature]
  MoveSource
  (next-move [this board ui-handler]
    (swap! calls conj ["next-move" board])
    (let [move (moves board)]
      (report-move ui-handler board move signature)
      (moves board))))

(defn random-move [board]
  [(rand-int (count board)) (rand-int (count board))])

(defn random-legal-move [board]
  (loop [current-move (random-move board)]
    (if (move-legal board current-move)
      current-move
      (recur (random-move board)))))

(defn new-dumb-ai-player
  ([moves signature]
   (DumbAiPlayer. (atom '()) moves signature))
  ([signature]
   (DumbAiPlayer. (atom '()) random-legal-move signature)))