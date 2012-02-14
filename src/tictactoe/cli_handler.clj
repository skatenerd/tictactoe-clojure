(ns tictactoe.cli-handler
  (:use [tictactoe.ui-handler]
        [tictactoe.board-utils])
  (:require [clojure.string :as string]))

(declare prompt-next-move prompt-next-move-with-warning print-board get-player)

(deftype ConsoleUIHandler []
  UIHandler
  (get-user-move-input [this board player]
    (prompt-next-move board player))
  (report-move [this board move player]
    (println "Compute's move was")
    (prn move player))
  (farewell [this board winner]
    (if winner
      (println (str "Game over, winner was " winner))
      (println "There was no winner"))
    (println "final board was ")
    (print-board board))
  (get-game-parameters [this]
    {:x (get-player :x)
     :o (get-player :o)})



  )

(def input-to-player-type
  {"1" :unbeatable-ai
   "2" :medium-ai
   "3" :dumb-ai
   "4" :human})


(def player-type-choices-strings
  (for [[key val] input-to-player-type]
    (str key " for " (name val))))


(defn get-player [signature]
  (println (str "Select player type for player " (name signature)))
  (doseq [option player-type-choices-strings] (println option))
  (loop [user-input (read-line)]
    (let [player-type (input-to-player-type  user-input)]
      (println "")
      (if player-type
        player-type
        (do
          (println "Please enter a valid option")
          (recur (read-line)))))))


(defn- print-row [idx row]
  (print (str idx))
  (doseq [square row]
    (print "|")
    (if square
      (print (str (name square) " "))
      (print "__")))
  (println "|"))


(defn- print-board [board]
  (println "  0  1  2")
  (loop [idx 0]
    (print-row idx (nth board idx))
    (if (< (inc idx) (count board))
      (recur (inc idx)))))

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
  (prompt-next-move board signature))

(defn new-cli-handler [] (ConsoleUIHandler. ))