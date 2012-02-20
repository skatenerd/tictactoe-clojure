(ns tictactoe.cli-handler-spec
  (:use [speclj.core]
        [tictactoe.ui-handler]
        [tictactoe.cli-handler]
        [tictactoe.board-utils]))

(describe "Get and parse CLI input"
  (with board [[nil :x nil]
               [nil :o nil]
               [:x nil :o ]])

  (with cli-handler (new-cli-handler))

  (context "parsing moves"
    (it "reads simple user input"
      (with-out-str
        (with-in-str "0 0"
          (should= [0 0] (get-user-move-input @cli-handler @board :x)))))

    (it "reads comma separated user input"
      (with-out-str
        (with-in-str "0, 0\n"
          (should= [0 0] (get-user-move-input @cli-handler @board :x)))
        (with-in-str "0,0\n"
          (should= [0 0] (get-user-move-input @cli-handler @board :x)))))

    (it "reads convoluted user input"
      (with-out-str
        (with-in-str "row 2 and column 1"
          (should= [2 1] (get-user-move-input @cli-handler @board :x)))))

    (it "reads single-integer user input"
      (with-out-str
        (with-in-str "2\n"
          (should= [0 2] (get-user-move-input @cli-handler @board :x)))))

    (it "parses input to row-col format"
      (should= [1 0] (parse-numbers '(3) 3))
      (should= [2 1] (parse-numbers '(7) 3))
      (should= [2 2] (parse-numbers '(2 2) 3))
      (should-not (parse-numbers '(1 1 1 1 1 1) 3)))

    )

  (context "parsing game setup"
    (it "parses player selection correctly"
      (with-out-str
        (with-in-str "7\n5\n1"
          (should= :unbeatable-ai (get-player :x)))
        (with-in-str "34\n87\n2"
          (should= :medium-ai (get-player :x)))
        (with-in-str "34\n87\n3"
          (should= :dumb-ai (get-player :x)))
        (with-in-str "34\n87\n4"
          (should= :human (get-player :x)))))

    (it "builds the game parameters correctly"
      (with-out-str
        (with-in-str "1\n554\n2\n88\n4"
          (should=
            {:x :unbeatable-ai
             :o :medium-ai
             :board-size 4}
            (get-game-parameters @cli-handler)))))))


(describe "human player moving with illegal moves"
  (with center-occupied-board (update-board (empty-board 3) [1 1] :x))
  (with cli-handler (new-cli-handler))
  (it "detects collisions"
    (with-out-str
      (with-in-str "1 1\n1 1\n1 1\n1 2"
        (should= [1 2] (get-user-move-input @cli-handler @center-occupied-board :x)))))
  (it "detects off-grid moves"
    (with-out-str
      (with-in-str "72 12\n2 2"
        (should= [2 2] (get-user-move-input @cli-handler @center-occupied-board :x)))))
  (it "deals with insane input"
    (with-out-str
      (with-in-str "1 2 3 4 5 6\n\n\n\n2"
        (should= [0 2] (get-user-move-input @cli-handler @center-occupied-board :x)))))
  )