(ns tictactoe.human-player-spec
  (:use [speclj.core]
        [tictactoe.human-player]
        [tictactoe.move-source]
        [tictactoe.board-utils :only [update-board empty-board]]))

(describe "Human player"
  (with player (new-human-player :o))
  (with board [[nil :x  nil]
               [nil :o  nil]
               [:x  nil :o]])
  (context "parsing"

    (it "reads simple user input"
      (binding [read-line (fn [] "0 0")]
        (should= [0 0] (next-move @player @board))))

    (it "reads comma separated user input"
      (with-in-str "0, 0"
        (should= [0 0] (next-move @player @board)))
      (with-in-str "0,0"
        (should= [0 0] (next-move @player @board))))

    (it "reads convoluted user input"
      (with-in-str "row 2 and column 1"
        (should= [2 1] (next-move @player @board))))))


(describe "human player moving with illegal moves"
  (with center-occupied-board (first (update-board empty-board [1 1] :x)))
  (with player-1 (new-human-player :o))
  (it "detects collisions"
    (with-in-str "1 1\n1 1\n1 1\n1 2"
      (should= [1 2] (next-move @player-1 @center-occupied-board))))
  (it "detects off-grid moves"
    (with-in-str "72 12\n2 2"
      (should= [2 2] (next-move @player-1 @center-occupied-board)))))