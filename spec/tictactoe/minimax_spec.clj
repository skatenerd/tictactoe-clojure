(ns tictactoe.minimax-spec
  (:use [speclj.core]
        [tictactoe.minimax]
        [tictactoe.board-utils :only [empty-board update-board]]
        [tictactoe.game-state]))



(describe "scoring and caching"
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







(context "scoring the board"
  (context "from perspective of player x"
    (it "scores based on current victor"
      (should= 1 (first (evaluate-board (new-game-state @x-won-col :x) {} :x nil))))

    (it "scores based on immediate victor"
      (should= 1 (first (evaluate-board (new-game-state @x-can-win-row :x) {} :x nil)))
      (should= 1 (first (evaluate-board (new-game-state @x-can-win-col :x) {} :x nil))))


    (it "recognizes guaranteed-ties"
      (should= 0 (first (evaluate-board (new-game-state @guaranteed-tie :o) {} :x nil))))

    (it "recognizes current ties"
      (should= 0 (first (evaluate-board (new-game-state @current-tie :x) {} :x nil))))

    (it "scores based on distant victor"
      (should= 1 (first (evaluate-board (new-game-state @x-can-win-smart :x) {} :x nil)))))


  (context "from the perspective of player o"
    (it "recognizes potential for opponent to throw game"
      (should= (/ 1 2) (first (evaluate-board
                                (new-game-state
                                  @guaranteed-tie
                                  :o)
                                {}
                                :o
                                nil))))
    (it "recognizes when game is over"
      (should= -1 (first (evaluate-board (new-game-state @x-can-win-row :x) {} :o nil))))
    ;(should= -1 (first (evaluate-board @x-can-win-col :x :o {})))
    ;(should= -1 (first (evaluate-board @x-won-row :x :o {})))

    ))

  (context "caching board scores"
    (context "private utilities"
      (it "scores a series of moves and caches the results"
        (let [x-can-win-state (new-game-state @x-can-win-row :x)
              x-won-state (new-game-state @x-won-row :o)]
          (should=
            [{x-won-state 1} [1]]
            (score-moves-and-cache-results x-can-win-state {} [[0 2]] :x nil)))))

    (context "from perspective of player x"
      (it "caches a win when it encounters one"
        (let [[score new-cached-situations] (evaluate-board (new-game-state @x-won-row :x) {} :x nil)]
          (should= {(new-game-state @x-won-row :x) 1} new-cached-situations)))

      (it "recognizes guaranteed-ties and caches top-level result"
        (let [[score learned-info] (evaluate-board (new-game-state @guaranteed-tie :o) {} :x nil)]
          (should= 0 (learned-info (new-game-state @guaranteed-tie :o)))))

      (it "recognizes guaranteed-ties and caches all explored results"
        (let [[score learned-info] (evaluate-board (new-game-state @guaranteed-tie :o) {} :x nil)
              explored-board (update-board @guaranteed-tie [2 1] :o)
              explored-state (new-game-state explored-board :x)]
          (should= 0 (learned-info explored-state))))

      (it "recognizes current ties"
        (let [[score learned-info] (evaluate-board (new-game-state @current-tie :x) {} :x nil)]
          (should=
            {(new-game-state @current-tie :x) 0
             (new-game-state @current-tie :o) 0}
            learned-info)))))


  (context "evaluating a move"
    (context "from perspective of player x"

      (it "scores a winning move"
        (should= 1 (first (evaluate-move (new-game-state @x-can-win-row :x) [0 2] {} :x nil))))

      (it "scores a distantly winning move"
        (let [game-state (new-game-state @x-can-win-smart :x)]
          (should= 1 (first (evaluate-move game-state [1 1] {} :x nil)))
          (should= -1 (first (evaluate-move game-state [0 2] {} :x nil)))))))
)

(describe "utilities"
  (it "adds to cache correctly"
    (let [game-state (new-game-state :foo :bar)]
      (should= {game-state 24} (add-to-cache {} game-state 24)))))