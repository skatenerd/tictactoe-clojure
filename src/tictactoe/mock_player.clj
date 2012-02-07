(ns tictactoe.mock-player
  (:use [tictactoe.move-source]))

(defrecord MockPlayer [calls moves signature]
  MoveSource
  (next-move [this board]
    (reset! calls (conj @calls ["next-move" board]))
    (moves board))
  (next-move-with-warning [this board]
    (reset! calls (conj @calls ["next-move-with-warning" board]))
    ([(rand-int 3) (rand-int 3)])))


(defn new-mock-player [moves signature]
  (MockPlayer. (atom'()) moves signature))