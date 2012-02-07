(ns tictactoe.game-runner
  (:use [tictactoe.board-utils :only [empty-board]]
        [tictactoe.move-source]))


(defn greet []
  (prn "Welcome to tic tac toe"))

(defn main-loop [player board]
  (prn "enter a move")
  (next-move player board))

(defprotocol Playable
  (run-game [this]))

(deftype GameRunner [player-1 player-2]
  Playable
  (run-game [this]
    (main-loop player-1 empty-board)))

(defn new-game-runner [player-1 player-2]
  (GameRunner. player-1 player-2))

