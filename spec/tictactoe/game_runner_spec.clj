(ns tictactoe.game-runner-spec
  (:use [speclj.core]
        [tictactoe.mock-player]
        [tictactoe.game-runner]
        [tictactoe.board-utils :only [empty-board]]))

(describe "game loop"
  (with player-1 (new-mock-player))
  (with player-2 (new-mock-player))
  (with game-runner (new-game-runner @player-1 @player-2))
  (it "prompts the first user for a move"
    (run-game @game-runner)
    (should= (list (list "next-move" empty-board)) @(.calls @player-1)
    ))
  (it "prompts the second user for a move with updated board"
    (let [after-one-move [[:x nil nil]
                          [nil nil nil]
                          [nil nil nil]]]
      (run-game @game-runner)
      (should= (list (list "next-move" empty-board)) @(.calls @player-1))
      (should= (list (list "next-move" after-one-move)) @(.calls @player-2))))


  )


