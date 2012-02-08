(ns tictactoe.human-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [move-legal print-board]])
  (:require [clojure.string :as string]))

(declare prompt-next-move-with-warning)

(defn prompt-next-move [board signature]
  (println "Please enter a move.")
  (println (str "Your alias is " (str signature)))
  (println "The current board is:")
  (print-board board)
  (println "")
  (let [input (read-line)
        splitted (string/split input #"[,\s]+")
        numbers (filter #(not (nil? (re-find #"[0-9]" %))) splitted)
        numbers (map read-string numbers)]
    (if (move-legal board numbers)
      numbers
      (prompt-next-move-with-warning board signature))))

(defn prompt-next-move-with-warning [board signature]
  (println "The previous move was invalid")
  (prompt-next-move board alias))

(deftype HumanPlayer [signature]
  MoveSource
  (next-move [this board]
    (prompt-next-move board signature))
  )

(defn new-human-player [signature]
  (HumanPlayer. signature))