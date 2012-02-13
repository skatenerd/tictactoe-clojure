(ns tictactoe.smart-ai-player-spec
  (:use [speclj.core]
        [tictactoe.smart-ai-player]
        [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-board]]
        [tictactoe.game-state]))

(describe "scoring boards and generating moves: "

  (with smart-ai-player (new-smart-ai-player :x))

  (with x-won-row [[:x :x :x]
                   [:o :o nil]
                   [:x :o nil]])

  (with x-won-col [[:x   nil :o]
                   [:x   :o  nil]
                   [:x nil nil]])

  (with x-can-win-row [[:x :x nil]
                       [:o :o nil]
                       [:x :o nil]])

  (with x-can-win-col [[:x   nil :o]
                       [:x   :o  nil]
                       [nil nil nil]])

  (with x-can-win-smart [[:o :x  nil]
                         [:x nil nil]
                         [:o nil nil]])

  (with x-can-win-really-smart [[:x  nil :o ]
                                [nil nil nil]
                                [nil nil nil]])

  (with cymen-board [[:x nil :o]
                     [:o nil nil]
                     [:x nil nil]])

  (with current-tie [[:x :o :o]
                     [:o :x :x]
                     [:x :o :o]])

  (with guaranteed-tie [[:o :x  nil]
                        [:x nil :o]
                        [:o nil :x ]])

  (context "generating moves"

    (it "moves based on imminent victories"
      (should= [0 2] (next-move @smart-ai-player @x-can-win-row))
      (should= [2 0] (next-move @smart-ai-player @x-can-win-col)))

    (it "moves based on distant guaranteed victories"
      (should= [1 1] (next-move @smart-ai-player @x-can-win-smart))
      (should (contains? #{[2 0] [2 2] [1 0]}  (next-move @smart-ai-player @x-can-win-really-smart))))

    (it "moves correctly with cymen's board"
      (should= [2 2] (next-move @smart-ai-player @cymen-board))))

  (context "scoring the board"
    (context "from perspective of player x"
      (it "scores based on current victor"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 1 (first (evaluate-board (new-game-state @x-won-col :x) {})))))

      (it "scores based on immediate victor"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 1 (first (evaluate-board (new-game-state @x-can-win-row :x) {})))
          (should= 1 (first (evaluate-board (new-game-state @x-can-win-col :x) {})))))


      (it "recognizes guaranteed-ties"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 0 (first (evaluate-board (new-game-state @guaranteed-tie :o) {})))))

      (it "recognizes current ties"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 0 (first (evaluate-board (new-game-state @current-tie :x) {})))))

      (it "scores based on distant victor"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 1 (first (evaluate-board (new-game-state @x-can-win-smart :x) {}))))))


      (context "from the perspective of player o"
        (it "recognizes potential for opponent to throw game"
          (binding [is-intended-winner (fn [signature] (= signature :o))]
            (should= (/ 1 2) (first (evaluate-board
                                      (new-game-state
                                        @guaranteed-tie
                                        :o)
                                      {})))))
        (it "recognizes when game is over"
          (binding [is-intended-winner (fn [signature] (= signature :o))]
            (should= -1 (first (evaluate-board (new-game-state @x-can-win-row :x) {})))))
        ;(should= -1 (first (evaluate-board @x-can-win-col :x :o {})))
        ;(should= -1 (first (evaluate-board @x-won-row :x :o {})))

        ))

  (context "caching board scores"
    (context "private utilities"
      (it "updates the total cache when it scores a move"
;        (let [x-won-state (new-game-state @x-won-row :o)]
;        (should=
;          [{(new-game-state @x-won-row :o) 1} [1]]
;          (score-and-cache-result [{} []] @x-can-win-row)
      )

      (it "scores a series of moves and caches the results"
        (let [x-can-win-state (new-game-state @x-can-win-row :x)
              x-won-state (new-game-state @x-won-row :o)]
          (binding [is-intended-winner (fn [player] (= player :x))]
            (should=
              [{x-won-state 1} [1]]
              (score-moves-and-cache-results x-can-win-state {} [[0 2]]))))))

    (context "from perspective of player x"
        (it "caches a win when it encounters one"
          (binding [is-intended-winner (fn [signature] (= signature :x))]
            (let [[score new-cached-situations] (evaluate-board (new-game-state @x-won-row :x) {})]
              (should= {(new-game-state @x-won-row :x) 1} new-cached-situations))))

        (it "recognizes guaranteed-ties"
          (binding [is-intended-winner (fn [signature] (= signature :x))]
            (let [[score learned-info] (evaluate-board (new-game-state @guaranteed-tie :o) {})]
              (should= {(new-game-state @guaranteed-tie :o) 0} learned-info))))

        (it "recognizes current ties"
          (binding [is-intended-winner (fn [signature] (= signature :x))]
            (let [[score learned-info] (evaluate-board (new-game-state @current-tie :x) {})]
              (should=
                {(new-game-state @current-tie :x) 0
                 (new-game-state @current-tie :o) 0}
                learned-info))))))


  (context "evaluating a move"
    (context "from perspective of player x"

      (it "scores a winning move"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 1 (first (evaluate-move (new-game-state @x-can-win-row :x) [0 2] {})))))

      (it "scores a distantly winning move"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (let [game-state (new-game-state @x-can-win-smart :x)]
          (should= 1 (first (evaluate-move game-state [1 1] {})))
          (should= -1 (first (evaluate-move game-state [0 2] {}))))))))
  )

(describe "utilities"
  (it "adds to cache correctly"
    (let [game-state (new-game-state :foo :bar)]
    (should= {game-state 24} (add-to-cache {} game-state 24)))))
