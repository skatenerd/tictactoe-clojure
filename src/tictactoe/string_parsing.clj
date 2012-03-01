(ns tictactoe.string-parsing)


(defn string-to-player [player-string]
  (case player-string
    "x"
    :x
    "o"
    :o
    nil))


(defn string-to-board [board-string]
  (vec
    (let [rows (partition 3 board-string)]
      (for [row rows]
        (vec (map string-to-player (map str row)))))))

