(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares]]))

;(defn winning-next-moves)

(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    [0 2]))


(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))

