(ns tictactoe.board-utils)

(def empty-board (vec (repeat 3 (vec (repeat 3 nil)))))

(defn move-within-bounds [board move]
  (every?
    #(and
      (< % (count board))
      (>= % 0))
    move))

(defn move-taken [board move]
  (get-in board move))

(defn move-legal [board move]
  (and
    (move-within-bounds board move)
    (not (move-taken board move))))

(defn update-board [board posn new-value]
  (if (move-legal board posn)
    (assoc-in board posn new-value)
    board))

(defn row-wins [board-size]
  (for [row (range board-size)]
    (for [col (range board-size)]
      [row col])))

(defn col-wins [board-size]
  (for [col (range board-size)]
    (for [row (range board-size)]
      [row col])))

(defn diag-wins [board-size]
  (list
    (for [row-and-col (range board-size)]
      [row-and-col row-and-col])
    (for [row-and-col (range board-size)]
      [row-and-col (- (dec board-size) row-and-col)])))

(defn path-winner [path board]
  (let [board-residents (map #(get-in board %) path)]
    (reduce #(if (= %1 %2) %1) board-residents)))

(defn win-paths []
  (concat (row-wins 3) (col-wins 3) (diag-wins 3)))

(defn game-winner [board]
  (some #(path-winner % board) (win-paths)))

(defn game-winner-after-move [board move signature]
  (game-winner (update-board board move signature)))

(defn all-squares [board-size]
  (apply
    concat
    (for [row (range board-size)]
      (for [col (range board-size)]
        [row col]))))

(defn empty-squares [board]
  (filter #(nil? (get-in board %)) (all-squares (count board))))

(defn potential-next-boards [board player]
  (map
    #(update-board board % player)
    (empty-squares board)))

(defn board-full? [board]
  (empty? (empty-squares board)))

(defn game-over? [board]
  (let [winner (game-winner board)
        full (board-full? board)]
    (or winner full)))
