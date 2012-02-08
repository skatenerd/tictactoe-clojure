(ns tictactoe.main
  (:use [tictactoe.mock-player :only [new-mock-player]]
        [tictactoe.human-player :only [new-human-player]]
        [tictactoe.game-runner]))

(defn -main []
  (let [human (new-human-player :o)
        machine (new-mock-player
                  :x)
        runner (new-game-runner machine human)]
    (run-game runner)))

(-main)