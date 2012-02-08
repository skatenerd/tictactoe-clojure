(ns tictactoe.game-runner
  (:use [tictactoe.board-utils :only [empty-board update-board game-over? farewell]]
        [tictactoe.move-source]))


(defn greet []
  (prn "Welcome to tic tac toe"))

(defn get-move-from-player [player current-board is-re-query?]
  (if is-re-query?
    (next-move-with-warning player current-board)
    (next-move player current-board)))

(defn main-loop [active-player inactive-player board is-re-query?]
  (let [active-player-move (get-move-from-player active-player board is-re-query?)
        [new-board valid-move?] (update-board board active-player-move (.signature active-player))
        invalid-move (not valid-move?)
        continue-game? (not (game-over? new-board))]
    (if continue-game?
      (if invalid-move
        (recur active-player inactive-player new-board invalid-move)
        (recur inactive-player active-player new-board invalid-move))
      (farewell new-board))))

(defprotocol Playable
  (run-game [this]))

(deftype GameRunner [player-1 player-2]
  Playable
  (run-game [this]
    (main-loop player-1 player-2 empty-board false)))

(defn new-game-runner [player-1 player-2]
  (GameRunner. player-1 player-2))

