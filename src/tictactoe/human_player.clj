(ns tictactoe.human-player
  (:use [tictactoe.move-source]))

(defn prompt-next-move [board]
  (prn "Please enter a move.  Current board is:")
  (doseq [row board]
    (prn row))
  (let [input (read-line)
        splitted (clojure.string/split input #"[,\s]+")
        numbers (filter #(not (nil? (re-find #"[0-9]" %))) splitted)
        numbers (map read-string numbers)]
    numbers))

(defn prompt-next-move-with-warning [board]
  (prn "The previous move was invalid")
  (prompt-next-move board))

(deftype HumanIo []
  MoveSource
  (next-move [this board]
    (prompt-next-move board))
  (next-move-with-warning [this board]
    (prompt-next-move-with-warning this board))
  )

(defn new-human-player []
  (HumanIo.))