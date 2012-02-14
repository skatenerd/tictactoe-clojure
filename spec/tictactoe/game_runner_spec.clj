(ns tictactoe.game-runner-spec
  (:use [speclj.core]
        [tictactoe.dumb-ai-player]
        [tictactoe.game-runner]
        [tictactoe.move-source]
        [tictactoe.ui-silencer]
        [tictactoe.board-utils :only [empty-board update-board legal-board-sizes]]))

(describe "simple game loop"
  (with player-1-first-move [1 1])
  (with player-2-first-move [2 2])
  (with board-size (first legal-board-sizes))
  (with starting-board (empty-board @board-size))
  (with board-after-first-turn (update-board @starting-board @player-1-first-move :x))
  (with player-1 (new-dumb-ai-player
                   (fn [board]
                     (if (= board @starting-board)
                       @player-1-first-move
                       [(rand-int (count board)) (rand-int (count board))]))
                   :x))
  (with player-2 (new-dumb-ai-player
                   (fn [board]
                     (if (= board @board-after-first-turn)
                       @player-2-first-move
                       [(rand-int (count board)) (rand-int (count board))]))
                   :o))


  (it "prompts the first user for a move, and prompts the second user for a move with updated board"
    (main-loop @player-1 @player-2 @starting-board (new-ui-silencer))
    (should= ["next-move" @starting-board] (last @(.calls @player-1)))
    (should= ["next-move" @board-after-first-turn] (last @(.calls @player-2))))
  (it "says farewell"
    (let [ui-handler (new-ui-silencer)]
      (main-loop @player-1 @player-2 @starting-board ui-handler)
      (should= "farewell" (first @(.calls ui-handler))))))



