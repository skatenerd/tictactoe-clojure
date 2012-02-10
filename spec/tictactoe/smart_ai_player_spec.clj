(ns tictactoe.smart-ai-player-spec
  (:use [speclj.core]
        [tictactoe.smart-ai-player]
        [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-board]]))

(describe "basic intelligence"
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

  (it "recognizes imminent victories"
    (should= [0 2] (next-move @smart-ai-player @x-can-win-row))
    (should= [2 0] (next-move @smart-ai-player @x-can-win-col)))

  (it "recognizes distant victories"
    (should= [1 1] (next-move @smart-ai-player @x-can-win-smart))
    (should (contains? #{[2 0] [2 2] [1 0]}  (next-move @smart-ai-player @x-can-win-really-smart)))
    )

  (it "does cymen's board correctly"
    (should= [2 2] (next-move @smart-ai-player @cymen-board)))

  (context "scoring the board"
    (it "scores based on current victor"

      (let [[score learned-info] (score-board @x-won-row :x :x {})]
        (should= 1 score)
        (should= {[@x-won-row :x] 1} learned-info))

      (should= -1 (first (score-board @x-won-row :x :o {})))
      (should= 1 (first (score-board @x-won-col :x :x {})))
      (should= -1 (first (score-board @x-won-col :x :o {}))))

    (it "scores based on immediate victor"
      (should= 1 (first (score-board @x-can-win-row :x :x {})))
      (should= -1 (first (score-board @x-can-win-row :x :o {})))
      (should= 1 (first (score-board @x-can-win-col :x :x {})))
      (should= -1 (first (score-board @x-can-win-col :x :o {})))
    )

    (it "scores based on distant victor"
      (should= 1 (first (score-board @x-can-win-smart :x :x {})))
      )

    (it "recognizes current ties"
      (let [[score learned-info] (score-board @current-tie :x :x {})]
        (should= 0 score)
        (should= {[@current-tie :x] 0 [@current-tie :o] 0} learned-info)))

    (it "recognizes guaranteed-ties, with potential for throwing game"
      (let [[score learned-info] (score-board @guaranteed-tie :o :x {})]
        (should= 0 score)
        (should= {[@guaranteed-tie :o] 0} learned-info))
      (should= (/ 1 2) (first (score-board @guaranteed-tie :o :o {})))
      )
)
  (context "scoring a move"
    (it "scores a winning move"
      (should= 1 (first (score-move @x-can-win-row [0 2] :x))))
    (it "scores a distantly winning move"
      (should= 1 (first (score-move @x-can-win-smart [1 1] :x)))
      (should= -1 (first (score-move @x-can-win-smart [0 2] :x)))
      )

    )
;  (context "scoring a collection of moves"
;    (it "evaluates scores on an empty collection of boards"
;      (let [mock-cache {:a :b}]
;        (should=
;          {:mock-cache mock-cache
;                  :learned-situations {}
;                  :scores []}
;          (evaluate-and-cache-boards boards mock-cache))))
;    (it "evaluates score of one board"
;      (let [mock-cache {:a :b}
;            learned-situation {[@current-tie :x] 0}
;            updated-cache (conj mock-cache learned-situation)]
;            (should=
;              {:mock-cache updated-cache
;                      :learned-situations learned-situatio
;                      :scores [0]}
;              (evaluate-and-cache-boards mock-cache)))))

  )

(describe "utilities"
  (it "adds to cache correctly"
    (should= {[:x :y] 24} (add-to-cache {} :x :y 24))
    )

  )
