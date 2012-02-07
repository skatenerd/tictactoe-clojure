(ns tictactoe.game-runner-spec
  (:use [speclj.core]
        [tictactoe.mock-player]
        [tictactoe.game-runner]
        [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-board update-board]]))

(describe "game loop"
  (with player-1 (new-mock-player {empty-board [1 1]} :x))
  (with player-2 (new-mock-player {} :o))
  (with board-after-first-turn (update-board empty-board (next-move @player-1 empty-board) :x))
  (with game-runner (new-game-runner @player-1 @player-2))

  (it "prompts the first user for a move"
    (run-game @game-runner)
    (should= (list (list "next-move" empty-board)) @(.calls @player-1)
    ))

  (it "prompts the second user for a move with updated board"
      (run-game @game-runner)
      (should= (list (list "next-move" empty-board)) @(.calls @player-1))
      (should= (list (list "next-move" @board-after-first-turn)) @(.calls @player-2)))


  )


