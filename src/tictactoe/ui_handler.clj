(ns tictactoe.ui-handler)

(defprotocol UIHandler
  (get-user-move-input [this board player])
  (report-move [this board move player])
  (farewell [this board winner])
  (get-game-parameters [this]))