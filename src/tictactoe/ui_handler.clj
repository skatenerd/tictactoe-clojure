(ns tictactoe.ui-handler)

(defprotocol UIHandler
  (get-user-move-input [this board player])
  (report-move [this board move player])
  (farewell [this board winner])
  (print-text [this text])
  (print-text-ln [this text])
  )













(deftype PrintHandler []
  UIHandler
  (print-text [this text] (print text))
  (print-text-ln [this text] (println text))
  )

(deftype UISilencer []
  UIHandler
  (print-text [this text] nil)
  (print-text-ln [this text] nil)
  (get-user-move-input [this board player] nil)
  (report-move [this board move player] nil)
  (farewell [this board winner] nil))

(defn new-ui-silencer []
  (UISilencer.))

(defn new-print-handler []
  (PrintHandler.))