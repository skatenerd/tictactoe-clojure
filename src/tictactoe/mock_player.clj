(ns tictactoe.mock-player
  (:use [tictactoe.move-source]))

(defrecord MockPlayer [calls moves signature]
  MoveSource
  (next-move [this board]
    (reset! calls (conj @calls (list "next-move" board)))
    (moves board)))


(defn new-mock-player [moves signature]
  (MockPlayer. (atom'()) moves signature))