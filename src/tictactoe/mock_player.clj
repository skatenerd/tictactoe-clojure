(ns tictactoe.mock-player
  (:use [tictactoe.move-source]))

(defn mock-decide-move [board]
  [(rand-int 3) (rand-int 3)])

(defrecord MockPlayer [calls]
  MoveSource
  (next-move [this board]
    (reset! calls (conj @calls (list "next-move" board)))
    (mock-decide-move board)))


(defn new-mock-player []
  (MockPlayer. (atom'())))