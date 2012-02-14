(ns tictactoe.main
  (:use [tictactoe.smart-ai-player :only [new-smart-ai-player]]
        [tictactoe.human-player :only [new-human-player]]
        [tictactoe.game-runner]
        [tictactoe.cli-handler]
        ))


(defn build-player [input signature]
  (let [ui-handler (new-cli-handler)]
    (case input
      "1"
      (new-smart-ai-player signature)
      "2"
      (new-human-player signature)
      nil)))

(defn get-player [warn signature]
  (println (str "Should Player " signature " be computer or human? 1 for mensch-machine 2 for human"))
  (loop [constructed-player (build-player (read-line) signature)]
      (if constructed-player
        constructed-player
        (do
          (print "Please enter 1 or 2")
          (recur (build-player (read-line) signature))))))

(defn main []
  (println "")
  (let [player-1 (get-player false :x)
        player-2 (get-player false :o)]
    (main-loop player-1 player-2 (new-cli-handler))))