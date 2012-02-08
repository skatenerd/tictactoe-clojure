(ns tictactoe.smart-ai-player-spec
  (:use [speclj.core]
        [tictactoe.smart-ai-player]
        [tictactoe.move-source]))

(describe "basic intelligence"
  (with smart-ai-player (new-smart-ai-player :x))
  (with x-can-win-row [[:x :x nil
                        :o :o nil
                        :x :o nil]])
  (with x-can-win-col [[:x   nil :o
                        :x   :o  nil
                        :nil nil nil]])

  (it "recognizes imminent victories"
    (should= [0 2] (next-move @smart-ai-player @x-can-win-row))
    (should= [2 0] (next-move @smart-ai-player @x-can-win-col)))

  )