(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.minimax :only [compute-next-move]]
        [tictactoe.ui-handler]))

(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board ui-handler]
    (let [next-move (compute-next-move board signature)]
      (report-move ui-handler board next-move signature)
      next-move)))

(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))
