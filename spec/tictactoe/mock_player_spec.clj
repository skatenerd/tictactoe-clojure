(ns tictactoe.mock-player-spec
  (:use [speclj.core]
        [tictactoe.mock-player]
        [tictactoe.move-source]))

(describe "mock player"
  (with player (new-mock-player))
  (with legal-moves (set (for [row (range 3)
                          col (range 3)]
                      [row col])))
  (with first-board [[:x nil :x]
                     [:o :o nil]
                     [:x :o nil]])
  (with second-board [[:x nil :x]
                      [:o :o  :x]
                      [:x :o  :o]])


  (it "moves randomly and legally"
    (let [made-moves (set (for [_ (range 5000)]
                            (next-move @player @first-board)))]
      (should= @legal-moves made-moves))
    )

  (it "remembers its calls in a queue"
    (next-move @player @first-board)
    (should= (list (list "next-move" @first-board)) @(.calls @player))
    (next-move @player @second-board)
    (should= (list (list "next-move" @second-board) (list "next-move" @first-board)) @(.calls @player))
    )

  (it "supports move choice override"
    (let [illegal-move [8 5]]
      (binding [mock-decide-move (fn [&_] [8 5])]
        (should= illegal-move (next-move @player @first-board)))))

  )