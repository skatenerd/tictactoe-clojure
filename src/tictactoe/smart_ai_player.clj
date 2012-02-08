(ns tictactoe.smart-ai-player
  (:use [tictactoe.move-source]
        [tictactoe.board-utils :only [empty-squares game-winner-after-move update-board]]))

(defn winning-next-moves [board signature]
  (let [next-moves (empty-squares board)]
    (filter #(= signature (game-winner-after-move board % signature)) next-moves)))

(defn winning-move [board signature]
  (first (winning-next-moves board signature)))

(deftype SmartAiPlayer [signature]
  MoveSource
  (next-move [this board]
    (let [winning-move (winning-move board signature)]
      (if winning-move
        winning-move
        (first (empty-squares board))))))


(defn new-smart-ai-player [signature]
  (SmartAiPlayer. signature))

