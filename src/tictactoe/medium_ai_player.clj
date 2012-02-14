(ns tictactoe.medium-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.minimax :only [compute-next-move]]
        [tictactoe.ui-handler]))

(def max-recursion-depth 3)

(deftype MediumAiPlayer [signature]
  MoveSource
  (next-move [this board ui-handler]
    (let [next-move (compute-next-move board signature max-recursion-depth)]
      (report-move ui-handler board next-move signature)
      next-move)))

(defn new-medium-ai-player [signature]
  (MediumAiPlayer. signature))


