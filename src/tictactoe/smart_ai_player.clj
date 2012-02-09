(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares potential-next-boards game-winner game-winner-after-move update-board]]))

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
  (let [winner
        (game-winner board)
        potential-future-boards
        (potential-next-boards board player)]
    (cond
      winner
      (get-score-from-winner intended-winner winner)
      (empty? potential-future-boards)
      0
      :else
      (let [other-player (other-player player)
            best-next-board-for-player (if
                                         (= player intended-winner)
                                         (apply
                                           max-key
                                           #(score-board
                                              %
                                              other-player
                                              intended-winner)
                                           potential-future-boards)
                                         (apply
                                           min-key
                                           #(score-board
                                              %
                                              other-player
                                              intended-winner)
                                           potential-future-boards))]
        (score-board
          best-next-board-for-player
          other-player
          intended-winner)))))

(defn score-move [board move player]
  (let [resulting-board (update-board board move player)]
    (score-board resulting-board (other-player  player) player)))

(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (let [score-of-board (score-board board signature signature)
          _ (prn "found score")
          optimal-moves (filter
                          #(=
                             (score-move board % signature)
                             score-of-board)
                          (empty-squares board))
          _ (prn "done filtering")]
      (first optimal-moves))))


(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))

