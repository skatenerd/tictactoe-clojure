(ns tictactoe.ui-silencer
  (:use [tictactoe.ui-handler]))

(deftype UISilencer [calls]
  UIHandler
  (report-move [this board move player]
    (swap! calls conj "report-move"))
  (farewell [this board winner]
    (swap! calls conj "farewell")))

(defn new-ui-silencer []
  (UISilencer. (atom '())))