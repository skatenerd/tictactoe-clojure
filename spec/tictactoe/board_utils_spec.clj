(ns tictactoe.board-utils-spec
  (:use [speclj.core]
        [tictactoe.board-utils]))

(describe "board utilities"
  (with x-in-corner [[:x nil nil]
                     [nil nil nil]
                     [nil nil nil]])
  (with x-and-o-on-board (assoc-in @x-in-corner [1 1] :o))
  (with row-loss [[:x :x :x]
                  [nil nil nil]
                  [nil nil nil]])
  (with col-loss [[:x nil nil]
                  [:x nil nil]
                  [:x nil nil]])
  (with diag-loss [[:x nil nil]
                   [nil :x nil]
                   [nil nil :x]])

  (context "updating board"

  (it "updates a blank board"
      (should= @x-in-corner (update-board empty-board [0 0] :x))
      (should= @x-in-corner (update-board @x-in-corner [0 0] :o))))

  (context "check for game over"
    (it "produces diag-win-paths"
      (should= (list
                 (list [0 0] [1 1] [2 2])
                 (list [0 2] [1 1] [2 0]))
               (diag-wins 3)))
    (it "produces col-win-paths"
      (should= (list
                 (list [0 0] [1 0] [2 0])
                 (list [0 1] [1 1] [2 1])
                 (list [0 2] [1 2] [2 2]))
               (col-wins 3)))

    (it "produces row win paths"
      (should= (list
                 (list [0 0] [0 1] [0 2])
                 (list [1 0] [1 1] [1 2])
                 (list [2 0] [2 1] [2 2]))
               (row-wins 3)))

    (it "checks correctly for a win on the first row"
      (let [top-row-win-path (first (row-wins 3))
            diag-win-path (first (diag-wins 3))]
        (should (is-winning-path top-row-win-path @top-row-win))
        (should-not (is-winning-path diag-win-path @top-row-win))))

    (it "says a game with two moves is not over"
      (prn (row-wins 3))
      (prn (col-wins 3))
      (should-not (game-over? @x-in-corner))
      (should-not (game-over? @x-and-o-on-board))
      (should (game-over? @col-loss))
      (should (game-over? @row-loss))
      (should (game-over? @diag-loss))))




)