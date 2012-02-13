(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.minimax :only [compute-next-move]]))

(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (let [next-move (compute-next-move board signature)]
      (println "Computer's move was:")
      (prn next-move signature)
      next-move)))

(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))
