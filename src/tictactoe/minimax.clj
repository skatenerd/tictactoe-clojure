(ns tictactoe.minimax
  (:use [tictactoe.board-utils :only [empty-squares
                                      potential-next-boards
                                      game-winner
                                      update-board]]
        [tictactoe.string-parsing]
        [tictactoe.game-state]))


;(deftype minimax-config [intended-winner max-recursion-depth])

(declare evaluate-board compute-next-move score-move evaluate-move add-to-cache)

(defn safe-dec [to-dec]
  (if to-dec
    (dec to-dec)))

(defn safe-zero? [to-check]
  (and to-check (zero? to-check)))

(defn violates-max-recursion-depth [depth max-depth]
  (if max-depth
    (> depth max-depth)
    false))

(defn is-intended-winner [winner intended-winner]
  (= winner intended-winner))

(defn compute-final-score [winner intended-winner]
  (if winner
    (if (is-intended-winner winner intended-winner)
      1
      -1)
    nil))

(defn compute-score-as-opponent [scores-of-future-boards]
  (if (contains? (set scores-of-future-boards) -1)
    -1
    (/ (apply + scores-of-future-boards)
      (count scores-of-future-boards))))

(defn prune [min-score max-score player intended-winner]
  (if (is-intended-winner player intended-winner)
    (= max-score 1)
    (= min-score -1)))

(defn score-move-and-cache-result [[cache scores min-score max-score] move game-state intended-winner remaining-moves-ahead]
  (if
    (prune min-score max-score (:player game-state) intended-winner)
    [cache scores min-score max-score]
    (let [[score updated-cache] (evaluate-move game-state move cache intended-winner remaining-moves-ahead)]
      [updated-cache
       (conj scores score)
       (min min-score score)
       (max max-score score)])))

(defn score-moves-and-cache-results [game-state current-cache moves intended-winner remaining-moves-ahead]
  (take 2
    (reduce #(score-move-and-cache-result
               %1
               %2
               game-state
               intended-winner
               remaining-moves-ahead)
      [current-cache [] 0 0] moves)))

(defn compute-score-from-achievable-scores [achievable-scores player intended-winner]
  (if (is-intended-winner player intended-winner)
    (apply max achievable-scores)
    (compute-score-as-opponent achievable-scores)))

(defn score-by-thinking-ahead [game-state cached-scores intended-winner remaining-moves-ahead]
  (let
    [possible-moves
     (empty-squares (:board game-state))
     [updated-cache scores-of-future-boards]
     (score-moves-and-cache-results game-state cached-scores possible-moves intended-winner remaining-moves-ahead)
     score-of-current-state (compute-score-from-achievable-scores
                              scores-of-future-boards
                              (:player game-state)
                              intended-winner)]

    [score-of-current-state (add-to-cache updated-cache game-state score-of-current-state)]))

(defn cache-tie [cache game-state]
  (-> cache
    (add-to-cache game-state 0)
    (add-to-cache (swap-player game-state) 0)))

(defn evaluate-board [game-state cached-scores intended-winner remaining-moves-ahead]
  (let [looked-up-winner
        (cached-scores game-state)
        computed-winner
        (game-winner (:board game-state))]
    (cond
      looked-up-winner
      [looked-up-winner cached-scores]
      computed-winner
      (let [score (compute-final-score computed-winner intended-winner)]
        [score (add-to-cache cached-scores game-state score)])
      (empty? (empty-squares (:board game-state)))
      [0 (cache-tie cached-scores game-state)]
      (safe-zero? remaining-moves-ahead)
      [0 (add-to-cache cached-scores game-state 0)]
      :else
      (score-by-thinking-ahead game-state cached-scores intended-winner (safe-dec remaining-moves-ahead)))))

(defn evaluate-move [game-state move cache intended-winner remaining-moves-ahead]
  (evaluate-board (apply-move game-state move) cache intended-winner remaining-moves-ahead))

(defn add-to-cache [cache game-state score]
  (conj cache {game-state score}))

(defn compute-next-move
  ([board player max-recursion-depth]
  (let [game-state (new-game-state board player)]
    (let [[score cache] (evaluate-board game-state {} player max-recursion-depth)
          possible-moves (empty-squares board)
          is-optimal-move #(=
                             score
                             (cache (apply-move game-state %)))
          optimal-moves (filter is-optimal-move possible-moves)]
      (if (empty? optimal-moves)
        nil
        (rand-nth optimal-moves)))))
  ([board player]
    (compute-next-move board player nil)))

(defn score-move [game-state move cache intended-winner remaining-moves-ahead]
  (let [[score cached-situations] (evaluate-move game-state move cache intended-winner remaining-moves-ahead)]
    score))