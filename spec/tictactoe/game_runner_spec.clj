(ns tictactoe.game-runner-spec
  (:use [speclj.core]
        [tictactoe.mock-player]
        [tictactoe.game-runner]
        [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-board update-board]]))

(describe "simple game loop"
  (with player-1-first-move [1 1])
  (with player-2-first-move [2 2])
  (with board-after-first-turn (first (update-board empty-board @player-1-first-move :x)))
  (with player-1 (new-mock-player
                   ;#(case % empty-board @player-1-first-move [(rand-int 3) (rand-int 3)])
                   (fn [board]
                     (if (= board empty-board)
                       @player-1-first-move
                       [(rand-int 3) (rand-int 3)]))
                   :x))
  (with player-2 (new-mock-player
                   (fn [board]
                     (if (= board @board-after-first-turn)
                       @player-2-first-move
                       [(rand-int 3) (rand-int 3)]))
                   :o))
  (with game-runner (new-game-runner @player-1 @player-2))


  (it "prompts the first user for a move, and prompts the second user for a move with updated board"
    (run-game @game-runner)
    (should= ["next-move" empty-board] (last @(.calls @player-1)))
    (should= ["next-move" @board-after-first-turn] (last @(.calls @player-2)))))

(describe "game loop with collisions"
  (with contention-square [1 1])
  (with board-after-first-turn (first (update-board empty-board @contention-square :x)))
  (with player-1 (new-mock-player
                   (fn [board]
                     (if (= board empty-board)
                       @contention-square
                       [(rand-int 3) (rand-int 3)]))
                   :x))

  (with player-2 (new-mock-player
                   (fn [board]
                     (if (= board @board-after-first-turn)
                       @contention-square
                       [(rand-int 3) (rand-int 3)]))
                   :o))
  (with game-runner (new-game-runner @player-1 @player-2))


  (it "prompts the first user for a move, and warns the second user when it plays an occupied square"
    (run-game @game-runner)
    (should= ["next-move" empty-board] (last @(.calls @player-1)))
    (should= ["next-move" @board-after-first-turn] (last @(.calls @player-2)))
    (let [second-to-last-move (last (drop-last @(.calls @player-2)))]
      (should= ["next-move-with-warning" @board-after-first-turn] second-to-last-move))))

(describe "game loop with off-board moves"
  (with illegal-move [17 1])
  (with player-1 (new-mock-player
                   (fn [board]
                     (if (= board empty-board)
                       @illegal-move
                       [(rand-int 3) (rand-int 3)]))
                   :x))

  (with player-2 (new-mock-player
                   (fn [board]
                     [(rand-int 3) (rand-int 3)])
                   :o))
  (with game-runner (new-game-runner @player-1 @player-2))


  (it "warns the player when an out of bounds move is made"
    (run-game @game-runner)
    (should= ["next-move" empty-board] (last @(.calls @player-1)))
    (let [second-to-last-move (last (drop-last @(.calls @player-1)))]
      (should= ["next-move-with-warning" empty-board] second-to-last-move))))



