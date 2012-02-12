(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares
                                      potential-next-boards
                                      game-winner
                                      game-winner-after-move
                                      update-board]]))

(declare evaluate-board score-move compute-next-move-as-player evaluate-move other-player add-to-cache)


(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (compute-next-move-as-player signature board))
  )

(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))






(defn compute-next-move-as-player [signature board]
  (let [possible-moves (empty-squares board)
        score-move-with-fixed-board #(score-move board % signature signature {})
        optimal-move (apply
                        max-key
                        score-move-with-fixed-board
                        possible-moves)]
    optimal-move)

  )


(defn score-move [board move player intended-winner cache]
  (let [[score cached-situations] (evaluate-move board move player cache intended-winner)]
    score)
  )

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

(defn compute-score-as-human [scores-of-future-boards]
  (if (contains? (set scores-of-future-boards) -1)
    -1
    (/ (count (filter #(= % 1) scores-of-future-boards))
      (count scores-of-future-boards)))
  )

(defn minimize-score-as-human [player intended-winner board cached-scores]
  (let
    [possible-moves (empty-squares board)
      scores-and-caches-of-future-boards
     (map
       #(evaluate-move board % player cached-scores intended-winner)
       possible-moves)
     scores-of-future-boards (map first scores-and-caches-of-future-boards)
     caches-of-future-boards (map second scores-and-caches-of-future-boards)]
    [(compute-score-as-human scores-of-future-boards) {}]
    ))

(defn maximize-score-rationally [player intended-winner board cached-scores]
  (let
    [possible-moves (empty-squares board)
      scores-and-caches-of-future-boards
     (map
       #(evaluate-move board % player cached-scores intended-winner)
       possible-moves)
     scores-of-future-boards (map first scores-and-caches-of-future-boards)
     caches-of-future-boards (map second scores-and-caches-of-future-boards)]
    [(apply max scores-of-future-boards) {}]))

(defn score-by-thinking-ahead [player intended-winner board cached-scores]
  (if (= player intended-winner)
    (maximize-score-rationally player intended-winner board cached-scores)
    (minimize-score-as-human player intended-winner board cached-scores)))

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


(defn evaluate-board [board player intended-winner cached-scores]
  (let [looked-up-winner
        (cached-scores [board player])
        computed-winner
        (game-winner board)]
    (cond
      looked-up-winner
      [looked-up-winner {}]
      computed-winner
      (let [score (get-score-from-winner intended-winner computed-winner)]
        [score (add-to-cache {} board player score)])
      (empty? (empty-squares board))
      [0 (->
        {}
        (add-to-cache board player 0)
        (add-to-cache board (other-player player) 0))]
      :else
      (score-by-thinking-ahead player intended-winner board cached-scores))))

(defn evaluate-move [board move player cache intended-winner]
  (let [resulting-board (update-board board move player)]
    (evaluate-board resulting-board (other-player player) intended-winner cache)))



(defn other-player [player]
  (case
    player
    :x
    :o
    :o
    :x))

(defn add-to-cache [cache board player score]
  (conj cache {[board player] score}))
