(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares
                                      potential-next-boards
                                      game-winner
                                      game-winner-after-move
                                      update-board]]
        [tictactoe.game-state]))

(def is-intended-winner)

(declare evaluate-board score-move compute-next-move-as-player evaluate-move other-player add-to-cache)


(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (compute-next-move-as-player (new-game-state board signature))))

(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))


(defn compute-next-move-as-player [game-state]
  (binding [is-intended-winner #(= % (:player game-state))]
    (let [possible-moves (empty-squares (:board game-state))
          score-move-with-fixed-board #(score-move game-state % {})
          optimal-move (apply
                          max-key
                          score-move-with-fixed-board
                          possible-moves)]
      optimal-move)))


(defn score-move [game-state move cache]
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

(defn minimize-score-as-human [game-state cached-scores]
  (let
    [possible-moves (empty-squares (:board game-state))
      scores-and-caches-of-future-boards
     (map
       #(evaluate-move game-state % cached-scores)
       possible-moves)
     scores-of-future-boards (map first scores-and-caches-of-future-boards)
     caches-of-future-boards (map second scores-and-caches-of-future-boards)]
    [(compute-score-as-human scores-of-future-boards) {}]))

(defn maximize-score-rationally [game-state cached-scores]
  (let
    [possible-moves (empty-squares (:board game-state))
      scores-and-caches-of-future-boards
     (map
       #(evaluate-move game-state % cached-scores)
       possible-moves)
     scores-of-future-boards (map first scores-and-caches-of-future-boards)
     caches-of-future-boards (map second scores-and-caches-of-future-boards)]
    [(apply max scores-of-future-boards) {}]))

(defn score-by-thinking-ahead [game-state cached-scores]
  (if (is-intended-winner (:player game-state))
    (maximize-score-rationally game-state cached-scores)
    (minimize-score-as-human game-state cached-scores)))

;(defn- evaluate-board-and-update-caches [player intended-winner {:keys [total-cache learned-situations scores]} board]
;  (let [[score new-situations-to-record] (evaluate-board board player total-cache)]
;    {:total-cache (conj total-cache new-situations-to-record)
;     :learned-situations new-situations-to-record
;     :scores (conj scores score)}))
;
;(defn- score-board-and-update-caches-as-player [player intended-winner]
;  (partial score-board-and-update-caches player intended-winner))
;
;(defn evaluate-and-cache-boards [boards cache current-player intended-winner]
;  (reduce
;    (score-board-and-update-caches-as-player (other-player current-player) intended-winner)
;    {:total-cache mock-cache
;     :learned-situations {}
;     :scores []}
;    boards))

(defn evaluate-board [game-state cached-scores]
  (let [looked-up-winner
        (cached-scores [game-state])
        computed-winner
        (game-winner (:board game-state))]
    (cond
      looked-up-winner
      [looked-up-winner {}]
      computed-winner
      (let [score (compute-final-score computed-winner)]
        [score (add-to-cache {} game-state score)])
      (empty? (empty-squares (:board game-state)))
      [0 (->
        {}
        (add-to-cache game-state 0)
        (add-to-cache (new-game-state (:board game-state) (other-player (:player game-state))) 0))]
      :else
      (score-by-thinking-ahead game-state cached-scores))))

(defn evaluate-move [game-state move cache]
  (let [resulting-board (update-board (:board game-state) move (:player game-state))
        other-player (other-player (:player game-state))
        resulting-game-state (new-game-state resulting-board other-player)]
    (evaluate-board resulting-game-state cache)))

(defn other-player [player]
  (case
    player
    :x
    :o
    :o
    :x))

(defn add-to-cache [cache game-state score]
  (conj cache {game-state score}))
