(ns tictactoe.smart-ai-player-spec
  (:use [speclj.core]
        [tictactoe.smart-ai-player]
        [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-board]]))

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
      (binding [is-intended-winner (fn [signature] (= signature :x))]
        (it "scores based on current victor"
          ;(should= -1 (first (evaluate-board @x-won-row :x :o {})))
          (should= 1 (first (evaluate-board @x-won-col :x {})))
          (should= -1 (first (evaluate-board @x-won-col :x {}))))

        (it "scores based on immediate victor"
          (should= 1 (first (evaluate-board @x-can-win-row :x {})))
          ;(should= -1 (first (evaluate-board @x-can-win-row :x :o {})))
          (should= 1 (first (evaluate-board @x-can-win-col :x {})))
          ;(should= -1 (first (evaluate-board @x-can-win-col :x :o {})))
          )
        (it "recognizes guaranteed-ties"
          (should= 0 (first (evaluate-board @guaranteed-tie :o {}))))

        (it "scores based on distant victor"
          (should= 1 (first (evaluate-board @x-can-win-smart :x {}))))

        (it "recognizes current ties"
          (should= 0 (first (evaluate-board @current-tie :x {}))))))

      (context "from the perspective of player o"
        (it "recognizes potential for opponent to throw game"
          (binding [is-intended-winner (fn [signature] (= signature :o))]
            (should= (/ 1 2) (first (evaluate-board @guaranteed-tie :o {})))))))

  (context "caching board scores"
    (context "from perspective of player x"
      (binding [is-intended-winner (fn [signature] (= signature :x))]

        (it "caches a win when it encounters one"
          (let [[score new-cached-situations] (evaluate-board @x-won-row :x {})]
            (should= {[@x-won-row :x] 1} new-cached-situations)))

        (it "recognizes guaranteed-ties, with potential for throwing game"
          (let [[score learned-info] (evaluate-board @guaranteed-tie :o {})]
            (should= {[@guaranteed-tie :o] 0} learned-info)))

        (it "recognizes current ties"
          (let [[score learned-info] (evaluate-board @current-tie :x {})]
            (should= {[@current-tie :x] 0 [@current-tie :o] 0} learned-info))))))


  (context "evaluating a move"
    (context "from perspective of player x"

      (it "scores a winning move"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 1 (first (evaluate-move @x-can-win-row [0 2] :x {})))))

      (it "scores a distantly winning move"
        (binding [is-intended-winner (fn [signature] (= signature :x))]
          (should= 1 (first (evaluate-move @x-can-win-smart [1 1] :x {})))
          (should= -1 (first (evaluate-move @x-can-win-smart [0 2] :x {})))))))

;  (context "scoring a collection of moves"
;    (it "evaluates scores on an empty collection of boards"
;      (let [mock-cache {:a :b}]
;        (should=
;          {:total-cache mock-cache
;           :learned-situations {}
;           :scores []}
;          (evaluate-and-cache-boards [] mock-cache :x))))
;    (it "evaluates score of one board"
;      (let [mock-cache {:a :b}
;            learned-situation {[@current-tie :x] 0}
;            updated-cache (conj mock-cache learned-situation)]
;            (should=
;              {:total-cache updated-cache
;               :learned-situations learned-situation
;               :scores [0]}
;              (evaluate-and-cache-boards [@current-tie] mock-cache :x))))
;    (it "evaluates score of multiple boards"
;      (let [mock-cache {:a :b}
;            learned-situations {[@current-tie :x] 0
;                                [@x-won-row :x] 1}
;            updated-cache (merge mock-cache learned-situations)]
;            (should=
;              {:total-cache updated-cache
;               :learned-situations learned-situations
;               :scores [0 1]}
;              (evaluate-and-cache-boards [@current-tie @x-won-row] mock-cache :x)))))
  )

(describe "utilities"
  (it "adds to cache correctly"
    (should= {[:x :y] 24} (add-to-cache {} :x :y 24))
    )

  (it "computes other player correctly"
    (should= :x (other-player :o))
    (should= :o (other-player :x))))
