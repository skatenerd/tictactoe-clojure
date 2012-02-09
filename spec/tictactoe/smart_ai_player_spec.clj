(ns tictactoe.smart-ai-player-spec
  (:use [speclj.core]
        [tictactoe.smart-ai-player]
        [tictactoe.move-source]))

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

  (it "recognizes imminent victories"
    (should= [0 2] (next-move @smart-ai-player @x-can-win-row))
    (should= [2 0] (next-move @smart-ai-player @x-can-win-col)))

  (it "recognizes distant victories"
    (should= [1 1] (next-move @smart-ai-player @x-can-win-row))
    )

  (context "scoring the board"
    (it "scores based on current victor"
      (should= 1 (score-board @x-won-row :x :x))
      (should= -1 (score-board @x-won-row :x :o))
      (should= 1 (score-board @x-won-col :x :x))
      (should= -1 (score-board @x-won-col :x :o)))

    (it "scores based on immediate victor"
      (should= 1 (score-board @x-can-win-row :x :x))
      (should= -1 (score-board @x-can-win-row :x :o))
      (should= 1 (score-board @x-can-win-col :x :x))
      (should= -1 (score-board @x-can-win-col :x :o))
    )

  ))
