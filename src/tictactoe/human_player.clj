(ns tictactoe.human-player
  (:use [tictactoe.move-source]))

(defn prompt-next-move [board]
  (let [input (read-line)
        splitted (clojure.string/split input #"[,\s]+")
        numbers (filter #(not (nil? (re-find #"[0-9]" %))) splitted)
        numbers (map read-string numbers)]
    numbers))

(deftype HumanIo []
  MoveSource
  (next-move [this board]
    (prompt-next-move board)))

(defn new-human-player []
  (HumanIo.))