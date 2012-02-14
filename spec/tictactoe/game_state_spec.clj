(ns tictactoe.game-state-spec
  (:use [speclj.core]
        [tictactoe.game-state]
        [tictactoe.board-utils]))

(describe "utility functions"
(it "computes other player correctly"
  (should= :x (other-player :o))
  (should= :o (other-player :x))))

(describe "mutators"

(it "swaps player correctly"
  (let [game-state (new-game-state :foo :x)]
    (should= (new-game-state :foo :o) (swap-player game-state))))

(it "applies a move correctly"
  (let [x-in-corner [[:x nil nil]
                     [nil nil nil]
                     [nil nil nil]]
        game-state (new-game-state (empty-board (count x-in-corner)) :x)]
    (should= (new-game-state x-in-corner :o) (apply-move game-state [0 0]))))

)
