(ns tictactoe.move-source)

(defprotocol MoveSource
  (next-move [this board ui-handler]))