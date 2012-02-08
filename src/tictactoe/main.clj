(ns tictactoe.main
  (:use [tictactoe.mock-player :only [new-mock-player]]
        [tictactoe.human-player :only [new-human-player]]
        [tictactoe.game-runner]))

(defn -main []
  (let [human (new-human-player)
        machine (new-mock-player
                  (fn [board]
                    [(rand-int 3) (rand-int 3)])
                  :x)
        runner (new-game-runner machine human)]
    (run-game runner)))