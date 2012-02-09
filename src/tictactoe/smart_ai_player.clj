(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares game-winner game-winner-after-move update-board]]))

(defn winning-next-moves [board signature]
  (let [next-moves (empty-squares board)]
    (filter #(= signature (game-winner-after-move board % signature)) next-moves)))

(defn winning-move [board signature]
  (first (winning-next-moves board signature)))

(defn get-score-from-winner [intended-winner winner]
  (if winner
    (if (= intended-winner winner)
      1
      -1)
    nil))

(defn other-player [player]
  (case
    player
    :x
    :o
    :o
    :x))

(defn score-board [board player intended-winner]
  (let [winner (game-winner board)
        potential-future-boards (map #(update-board board % player) (empty-squares board))]

    (cond
      winner
      (get-score-from-winner intended-winner winner)
      (empty? potential-future-boards)
      0
      :else
     (let [other-player (other-player player)
           best-next-board-for-player (if
                                        (= player intended-winner)
                                        (apply max-key #(score-board % other-player intended-winner) potential-future-boards)
                                        (apply min-key #(score-board % other-player intended-winner) potential-future-boards))]
       (score-board best-next-board-for-player other-player intended-winner)



      ))))


(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (let [winning-move (winning-move board signature)]
      (if winning-move
        winning-move
        (first (empty-squares board))))))


(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))

