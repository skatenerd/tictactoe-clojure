(ns tictactoe.smart-ai-player-spec
  (:use [speclj.core]
        [tictactoe.smart-ai-player]
        [tictactoe.move-source]
        [tictactoe.ui-handler]))


(describe "scoring boards and generating moves: "

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

  (with smart-ai-player (new-smart-ai-player :x))

  (with ui-handler (new-ui-silencer))

  (context "generating moves"

    (it "moves based on imminent victories"
      (should= [0 2] (next-move @smart-ai-player @x-can-win-row @ui-handler))
      (should= [2 0] (next-move @smart-ai-player @x-can-win-col @ui-handler)))

    (it "moves based on distant guaranteed victories"
      (should= [1 1] (next-move @smart-ai-player @x-can-win-smart @ui-handler))
      (should (contains? #{[2 0] [2 2] [1 0]}  (next-move @smart-ai-player @x-can-win-really-smart @ui-handler))))

    (it "moves correctly with cymen's board"
      (should= [2 2] (next-move @smart-ai-player @cymen-board  @ui-handler)))))


