(ns tictactoe.game-state
  (:use tictactoe.board-utils))
  ;(:use [tictactoe.board-utils [:only update-board]]))

(defrecord GameState [board player])

(defn other-player [player]
  (case
    player
    :x
    :o
    :o
    :x))


(defn new-game-state [board player]
  (GameState. board player))

(defn swap-player [game-state]
  (new-game-state (:board game-state) (other-player (:player game-state))))

(defn apply-move [game-state move]
  (new-game-state
    (update-board (:board game-state) move (:player game-state))
    (other-player (:player game-state))))