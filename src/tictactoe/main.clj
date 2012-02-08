(ns tictactoe.main
  (:use [tictactoe.dumb-ai-player :only [new-dumb-ai-player]]
        [tictactoe.human-player :only [new-human-player]]
        [tictactoe.game-runner]))


(defn build-player [input signature]
  (case input
    "1"
    (new-dumb-ai-player signature)
    "2"
    (new-human-player signature)
    nil))


(defn get-player [warn signature]
  (if
    warn
    (println "Please enter 1 or 2")
    (println (str "Should Player " signature " be computer or human? 1 for mensch-machine 2 for human")))
  (let [input (read-line)
        constructed-player (build-player input signature)]
    (if constructed-player
      constructed-player
      (recur true signature))))

(defn -main []
  (println "")
  (let [player-1 (get-player false :x)
        player-2 (get-player false :o)
        runner (new-game-runner player-1 player-2)]
    (run-game runner)))

;(-main)