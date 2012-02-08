(ns tictactoe.mock-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [move-legal]]))

(defrecord MockPlayer [calls moves signature]
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

(defn new-mock-player
  ([moves signature]
   (MockPlayer. (atom '()) moves signature))
  ([signature]
   (MockPlayer. (atom '()) random-legal-move signature)))