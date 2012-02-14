(ns tictactoe.human-player
  (:use [tictactoe.move-source]
        [tictactoe.ui-handler])
  (:require [clojure.string :as string]))


(deftype HumanPlayer [signature]
  MoveSource
  (next-move [this board ui-handler]
    (get-user-move-input ui-handler board signature))
  )

(defn new-human-player [signature]
  (HumanPlayer. signature))