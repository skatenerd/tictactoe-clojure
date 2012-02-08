(ns tictactoe.human-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [move-legal]])
  (:require [clojure.string :as string]))

(declare prompt-next-move-with-warning)

(defn prompt-next-move [board]
  (prn "Please enter a move.  Current board is:")
  (doseq [row board]
    (prn row))
  (let [input (read-line)
        splitted (string/split input #"[,\s]+")
        numbers (filter #(not (nil? (re-find #"[0-9]" %))) splitted)
        numbers (map read-string numbers)]
    (if (move-legal board numbers)
      numbers
      (prompt-next-move-with-warning board))))

(defn prompt-next-move-with-warning [board]
  (prn "The previous move was invalid")
  (prompt-next-move board))

(deftype HumanIo [signature]
  MoveSource
  (next-move [this board]
    (prompt-next-move board))
  )

(defn new-human-player [signature]
  (HumanIo. signature))