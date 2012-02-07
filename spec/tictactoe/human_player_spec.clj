(ns tictactoe.human-player-spec
  (:use [speclj.core]
        [tictactoe.human-player]
        [tictactoe.move-source]))

(describe "Human player"
  (with player (new-human-player))
  (with board [[nil :x  nil]
               [nil :o  nil]
               [:x  nil :o]])
  (context "parsing"
  (it "reads user input"
    (binding [read-line (fn [] "1 1")]
    (should= [1 1] (next-move @player @board))))))
