(ns tictactoe.game-runner
  (:use [tictactoe.board-utils :only [empty-board update-board]]
        [tictactoe.move-source]))


(defn greet []
  (prn "Welcome to tic tac toe"))

(defn main-loop [active-player inactive-player board]
  (let [active-player-move (next-move active-player board)
        new-board (update-board board active-player-move (.signature active-player))]
    (next-move inactive-player new-board)))

(defprotocol Playable
  (run-game [this]))

(deftype GameRunner [player-1 player-2]
  Playable
  (run-game [this]
    (main-loop player-1 player-2 empty-board)))

(defn new-game-runner [player-1 player-2]
  (GameRunner. player-1 player-2))

