(ns tictactoe.move-source)

(defprotocol MoveSource
  (next-move [this board])
  (next-move-with-warning [this board]))