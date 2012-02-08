(ns tictactoe.dumb-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [move-legal]]))

(defrecord DumbAIPlayer [calls moves signature]
  MoveSource
  (next-move [this board]
    (reset! calls (conj @calls ["next-move" board]))
    (moves board)))

(defn random-move []
  [(rand-int 3) (rand-int 3)])

(defn random-legal-move [board]
  (loop [current-move (random-move)]
    (if (move-legal board current-move)
      current-move
      (recur (random-move)))))

(defn new-dumb-ai-player
  ([moves signature]
   (DumbAIPlayer. (atom '()) moves signature))
  ([signature]
   (DumbAIPlayer. (atom '()) random-legal-move signature)))