(ns tictactoe.dumb-ai-player_spec
  (:use [speclj.core]
        [tictactoe.dumb-ai-player]
        [tictactoe.move-source]
        [tictactoe.board-utils]
        [tictactoe.ui-silencer]))

(describe "mock player"
  (with legal-moves (set (for [row (range 3)
                          col (range 3)]
                      [row col])))
  (with first-board [[:x nil :x]
                     [:o :o nil]
                     [:x :o nil]])
  (with first-board-remaining-moves
    (set [[0 1] [1 2] [2 2]]))
  (with second-board [[:x nil :x]
                      [:o :o  :x]
                      [:x :o  :o]])

  (with random-behaving-player (new-dumb-ai-player :x))

  (with ui-handler (new-ui-silencer))


  (it "moves randomly and legally, if we tell it to"
    (let [made-moves (set (for [_ (range 60)]
                            (next-move @random-behaving-player empty-board @ui-handler)))]
      (should= @legal-moves made-moves))

    (let [made-moves (set (for [_ (range 60)]
                            (next-move @random-behaving-player @first-board @ui-handler)))]
      (should= @first-board-remaining-moves made-moves))
    )

  (it "remembers its calls in a queue"
    (next-move @random-behaving-player @first-board  @ui-handler)
    (should= (list ["next-move" @first-board]) @(.calls @random-behaving-player))
    (next-move @random-behaving-player @second-board  @ui-handler)
    (should= (list ["next-move" @second-board] ["next-move" @first-board]) @(.calls @random-behaving-player))
    )

  (it "supports move choice override"
    (let [illegal-move [8 5]
          moves (fn [&_] illegal-move)
          illegal-behaving-player (new-dumb-ai-player moves :x)]
        (should= illegal-move (next-move illegal-behaving-player @first-board @ui-handler)))
    )


  )