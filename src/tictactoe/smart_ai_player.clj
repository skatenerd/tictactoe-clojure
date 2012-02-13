(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares
                                      potential-next-boards
                                      game-winner
                                      game-winner-after-move
                                      update-board]]
        [tictactoe.game-state]))

(def is-intended-winner)

(declare evaluate-board score-move compute-next-move evaluate-move add-to-cache)


(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (compute-next-move (new-game-state board signature))))

(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))


(defn compute-next-move [game-state]
  (binding [is-intended-winner #(= % (:player game-state))]
    (let [possible-moves (empty-squares (:board game-state))
          _ (prn possible-moves)
          score-move-with-fixed-board #(score-move game-state % {})
          optimal-move (apply
                          max-key
                          score-move-with-fixed-board
                          possible-moves)]
      optimal-move)))


(defn score-move [game-state move cache]
  (prn move)
  (let [[score cached-situations] (evaluate-move game-state move cache)]
    score))

(defn winning-next-moves [game-state]
  (let [next-moves (empty-squares (:board game-state))]
    (filter #(= (:player game-state) (game-winner-after-move game-state %)) next-moves)))

(defn compute-final-score [winner]
  (if winner
    (if (is-intended-winner winner)
      1
      -1)
    nil))

(defn compute-score-as-human [scores-of-future-boards]
  (if (contains? (set scores-of-future-boards) -1)
    -1
    (/ (count (filter #(= % 1) scores-of-future-boards))
      (count scores-of-future-boards))))

(defn score-move-and-cache-result [game-state [cache scores] move]
  (let [[score updated-cache] (evaluate-move game-state move cache)]
    [updated-cache (conj scores score)]))

(defn score-moves-and-cache-results [game-state current-cache moves]
  (reduce #(score-move-and-cache-result game-state %1 %2) [current-cache []] moves))

(defn minimize-score-as-human [game-state cached-scores]
  (let
    [possible-moves
     (empty-squares (:board game-state))
     [updated-cache scores-of-future-boards]
     (score-moves-and-cache-results game-state cached-scores possible-moves)
     score-of-current-state (compute-score-as-human scores-of-future-boards)]

    [score-of-current-state (add-to-cache cached-scores game-state score-of-current-state)]))

(defn maximize-score-rationally [game-state cached-scores]
  (let
    [possible-moves
     (empty-squares (:board game-state))
     [updated-cache scores-of-future-boards]
     (score-moves-and-cache-results game-state cached-scores possible-moves)
     score-of-current-state (apply max scores-of-future-boards)]

    [score-of-current-state (add-to-cache cached-scores game-state score-of-current-state)]))

(defn score-by-thinking-ahead [game-state cached-scores]
  (if (is-intended-winner (:player game-state))
    (maximize-score-rationally game-state cached-scores)
    (minimize-score-as-human game-state cached-scores)))

(defn cache-tie [cache game-state]
  (-> cache
    (add-to-cache game-state 0)
    (add-to-cache (swap-player game-state) 0)))

(defn evaluate-board [game-state cached-scores]
  (let [looked-up-winner
        (cached-scores [game-state])
        computed-winner
        (game-winner (:board game-state))]
    (cond
      looked-up-winner
      [looked-up-winner cached-scores]
      computed-winner
      (let [score (compute-final-score computed-winner)]
        [score (add-to-cache cached-scores game-state score)])
      (empty? (empty-squares (:board game-state)))
      [0 (cache-tie cached-scores game-state)]
      :else
      (score-by-thinking-ahead game-state cached-scores))))

(defn evaluate-move [game-state move cache]
    (evaluate-board (apply-move game-state move) cache))

(defn add-to-cache [cache game-state score]
  (conj cache {game-state score}))
