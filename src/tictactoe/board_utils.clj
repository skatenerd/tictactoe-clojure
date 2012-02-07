(ns tictactoe.board-utils)

(def empty-board (vec (repeat 3 (vec (repeat 3 nil)))))

(defn update-board [board posn new-value]
  (assoc-in board posn new-value))