(ns tictactoe.human-player-spec
  (:use [speclj.core]
        [tictactoe.human-player]
        [tictactoe.move-source]))

(describe "Human player"
  (with player (new-human-player :o))
  (with board [[nil :x  nil]
               [nil :o  nil]
               [:x  nil :o]])
  (context "parsing"

    (it "reads simple user input"
      (binding [read-line (fn [] "1 1")]
        (should= [1 1] (next-move @player @board))))

    (it "reads comma separated user input"
      (binding [read-line (fn [] "1, 1")]
        (should= [1 1] (next-move @player @board)))
      (binding [read-line (fn [] "1,1")]
        (should= [1 1] (next-move @player @board))))

    (it "reads convoluted user input"
      (binding [read-line (fn [] "row 2 and column 1")]
        (should= [2 1] (next-move @player @board))))))
