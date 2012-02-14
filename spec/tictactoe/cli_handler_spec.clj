(ns tictactoe.cli-handler-spec
  (:use [speclj.core]
        [tictactoe.ui-handler]
        [tictactoe.cli-handler]
        [tictactoe.board-utils]))

(describe "Get and parse CLI input"
  (with board [[nil :x  nil]
               [nil :o  nil]
               [:x  nil :o]])

  (with cli-handler (new-cli-handler))

  (context "parsing"
    (it "reads simple user input"
      (with-out-str
        (with-in-str "0 0"
          (should= [0 0] (get-user-move-input @cli-handler @board :x)))))

    (it "reads comma separated user input"
      (with-out-str
        (with-in-str "0, 0"
          (should= [0 0] (get-user-move-input @cli-handler @board :x)))
        (with-in-str "0,0"
          (should= [0 0] (get-user-move-input @cli-handler @board :x)))))

    (it "reads convoluted user input"
      (with-out-str
        (with-in-str "row 2 and column 1"
          (should= [2 1] (get-user-move-input @cli-handler @board :x)))))))

(describe "human player moving with illegal moves"
  (with center-occupied-board (update-board empty-board [1 1] :x))
  (with cli-handler (new-cli-handler))
  (it "detects collisions"
    (with-out-str
      (with-in-str "1 1\n1 1\n1 1\n1 2"
        (should= [1 2] (get-user-move-input @cli-handler @center-occupied-board :x)))))
  (it "detects off-grid moves"
    (with-out-str
      (with-in-str "72 12\n2 2"
        (should= [2 2] (get-user-move-input @cli-handler @center-occupied-board :x))))))