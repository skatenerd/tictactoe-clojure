(ns tictactoe.board-utils)

(def empty-board (vec (repeat 3 (vec (repeat 3 nil)))))

(defn update-board [board posn new-value]
  (if (get-in board posn)
    board
    (assoc-in board posn new-value)))

(defn row-wins [board-size]
  (for [row (range board-size)]
    (for [col (range board-size)]
      [row col])))

(defn col-wins [board-size]
  (for [col (range board-size)]
    (for [row (range board-size)]
      [row col])))

(defn is-winning-path [path board]
  (let [board-residents (map #(get-in board %) path)]
    (prn board-residents)
    ))

(defn diag-wins [board-size]
  (list
    (for [row-and-col (range board-size)]
      [row-and-col row-and-col])
    (for [row-and-col (range board-size)]
      [row-and-col (- (dec board-size) row-and-col)])))

(defn game-over? [board]
  true)
  ;(> (num-moves-made  board) 5))