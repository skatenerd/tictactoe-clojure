(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares
                                      potential-next-boards
                                      game-winner
                                      game-winner-after-move
                                      update-board]]))

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


(defn simulate-dumb-player-score-assignment [player intended-winner achievable-boards]
  (let
    [score-board-from-opponent-perspective
     #(score-board
        %
        (other-player player)
        intended-winner)
     scores-of-future-boards
     (map
       score-board-from-opponent-perspective
       achievable-boards)]
    (if (contains? (set scores-of-future-boards) -1)
      -1
      (/ (count (filter #(= % 1) scores-of-future-boards))
        (count scores-of-future-boards)))))

(defn maximize-score-rationally [player intended-winner achievable-boards]
  (let
    [score-board-from-opponent-perspective
     #(score-board
        %
        (other-player player)
        intended-winner)
     best-achievable-score
     (apply
      max
      (map
        score-board-from-opponent-perspective
        achievable-boards))]
    best-achievable-score))

(defn score-by-thinking-ahead [player intended-winner achievable-boards]
  (if (= player intended-winner)
    (maximize-score-rationally player intended-winner achievable-boards)
    (simulate-dumb-player-score-assignment player intended-winner achievable-boards)))


(defn score-board [board player intended-winner]
  (let [winner
        (game-winner board)
        achievable-boards
        (potential-next-boards board player)]
    (cond
      winner
      (get-score-from-winner intended-winner winner)
      (empty? achievable-boards)
      0
      :else
      (score-by-thinking-ahead player intended-winner achievable-boards))))

(defn score-move [board move player]
  (let [resulting-board (update-board board move player)]
    (score-board resulting-board (other-player  player) player)))

(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (let [possible-moves (empty-squares board)
          score-move-fn #(score-move board % signature)
          optimal-move (max-key
                          score-move-fn
                          possible-moves)]
      optimal-move)))


(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))

