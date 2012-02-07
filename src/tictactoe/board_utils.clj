(ns tictactoe.board-utils)

(def empty-board (vec (repeat 3 (vec (repeat 3 nil)))))

(defn update-board [board posn new-value]
  (assoc-in board posn new-value))

(defn- num-non-nil [l]
  (count (filter #(not (nil? %)) l)))
(defn num-moves-made [board]
  (reduce + (map num-non-nil board)))


(defn game-over? [board]
  (> (num-moves-made  board) 5))