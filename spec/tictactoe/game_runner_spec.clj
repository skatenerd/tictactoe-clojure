(ns tictactoe.game-runner-spec
  (:use [speclj.core]
        [tictactoe.mock-player]
        [tictactoe.game-runner]
        [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-board update-board]]))

(describe "game loop"
  (with player-1-first-move [1 1])
  (with player-2-first-move [2 2])
  (with player-1 (new-mock-player
                   ;#(case % empty-board @player-1-first-move [(rand-int 3) (rand-int 3)])
                   (fn [board]
                     (if (= board empty-board)
                       @player-1-first-move
                       [(rand-int 3) (rand-int 3)]))
                   :x))
  (with board-after-first-turn (update-board empty-board @player-1-first-move :x))
  (with player-2 (new-mock-player
                   (fn [board]
                     (if (= board @board-after-first-turn)
                       @player-2-first-move
                       [(rand-int 3) (rand-int 3)]))
                   :o))
  (with game-runner (new-game-runner @player-1 @player-2))


  (it "prompts the first user for a move, and prompts the second user for a move with updated board"
      (run-game @game-runner)
      (should= (list (list "next-move" empty-board)) @(.calls @player-1))
      (should= (list (list "next-move" @board-after-first-turn)) @(.calls @player-2))))


