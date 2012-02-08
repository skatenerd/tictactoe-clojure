(ns tictactoe.board-utils-spec
  (:use [speclj.core]
        [tictactoe.board-utils]))

(describe "board utilities"
  (with x-in-corner [[:x nil nil]
                     [nil nil nil]
                     [nil nil nil]])
  (with x-and-o-on-board (assoc-in @x-in-corner [1 1] :o))
  (with row-win [[:x :x :x]
                 [nil nil nil]
                 [nil nil nil]])
  (with col-win [[:x nil nil]
                 [:x nil nil]
                 [:x nil nil]])
  (with diag-win [[:x nil nil]
                  [nil :x nil]
                  [nil nil :x]])
  (with full-board [[:o :x :o]
                     [:o :x :o]
                     [:o :x :o]])

  (context "updating board"

  (it "updates a blank board"
      (should= [@x-in-corner true] (update-board empty-board [0 0] :x)))

  (it "tells you when a move is illegal"
      (should= [@x-in-corner false] (update-board @x-in-corner [0 0] :x))
      (should= [empty-board false] (update-board empty-board [74 11] :o)))
    )

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
        (should (path-winner top-row-win-path @row-win))
        (should-not (path-winner diag-win-path @row-win))))

    (it "checks correctly for a diagonal win"
      (let [diag-win-path (first (diag-wins 3))
            top-row-win-path (first (row-wins 3))]
        (should (path-winner diag-win-path @diag-win))
        (should-not (path-winner top-row-win-path @diag-win))))

    (it "says a game with two moves is not over"
      (should-not (game-over? @x-in-corner))
      (should-not (game-over? @x-and-o-on-board)))
  
    (it "recognizes row, diag, and col winners"
      (should= :x (game-winner @col-win))
      (should= :x (game-winner @row-win))
      (should= :x (game-winner @diag-win))
      (should-not (game-winner @x-in-corner))
      (should-not (game-winner @x-and-o-on-board)))

    (it "recognizes a full board"
      (should (board-full? @full-board))
      (should-not (board-full? @col-win)))
    )




)