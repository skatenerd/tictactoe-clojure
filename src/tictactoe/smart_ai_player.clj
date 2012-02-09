(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares potential-next-boards game-winner game-winner-after-move update-board]]))

(declare score-board)

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


(defn best-next-board-for-player [next-boards player intended-winner]
  (let [goal-fn (if (= player intended-winner)
                  max-key
                  min-key)]

    (apply
      goal-fn
      #(score-board
         %
         (other-player player)
         intended-winner)
      next-boards)))


(defn score-board [board player intended-winner]
  (let [winner
        (game-winner board)
        next-boards
        (potential-next-boards board player)]
    (cond
      winner
      (get-score-from-winner intended-winner winner)
      (empty? next-boards)
      0
      :else
      (let [best-next-board (best-next-board-for-player
                              next-boards
                              player
                              intended-winner)]
        (score-board
          best-next-board
          (other-player player)
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

