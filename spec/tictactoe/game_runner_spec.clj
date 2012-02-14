(ns tictactoe.game-runner-spec
  (:use [speclj.core]
        [tictactoe.dumb-ai-player]
        [tictactoe.game-runner]
        [tictactoe.move-source]
        [tictactoe.ui-handler]
        [tictactoe.board-utils :only [empty-board update-board]]))

(describe "simple game loop"
  (with player-1-first-move [1 1])
  (with player-2-first-move [2 2])
  (with board-after-first-turn (update-board empty-board @player-1-first-move :x))
  (with player-1 (new-dumb-ai-player
                   (fn [board]
                     (if (= board empty-board)
                       @player-1-first-move
                       [(rand-int 3) (rand-int 3)]))
                   :x))
  (with player-2 (new-dumb-ai-player
                   (fn [board]
                     (if (= board @board-after-first-turn)
                       @player-2-first-move
                       [(rand-int 3) (rand-int 3)]))
                   :o))


  (it "prompts the first user for a move, and prompts the second user for a move with updated board"
    (main-loop @player-1 @player-2 empty-board (new-ui-silencer))
    (should= ["next-move" empty-board] (last @(.calls @player-1)))
    (should= ["next-move" @board-after-first-turn] (last @(.calls @player-2)))))



