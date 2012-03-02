(ns tictactoe.board-utils)

(def legal-board-sizes [3 4])

(defn empty-board [size]
  (vec (repeat size (vec (repeat size nil)))))

(defn subset? [sub super]
  (every? #(contains? super %) sub))

(defn board-legal [board]
  (and
    (not (empty? board))
    (apply = (map count board))
    (contains? (set legal-board-sizes) (count (first board)))
    (contains? (set legal-board-sizes) (count board))
    (= (count board) (count (first board)))
    (subset? (set (flatten board)) #{:x :o nil})))


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
    move
    (move-within-bounds board move)
    (not (move-taken board move))))

(defn update-board [board posn new-value]
  (if (move-legal board posn)
    (assoc-in
      board
      posn
      new-value)
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

(defn win-paths [board]
  (let [board-size (count board)]
    (concat (row-wins board-size) (col-wins board-size) (diag-wins board-size))))

(defn game-winner [board]
  (some #(path-winner % board) (win-paths board)))

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
