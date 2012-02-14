(ns tictactoe.main
  (:use [tictactoe.game-runner]
        [tictactoe.cli-handler]
        [tictactoe.ui-handler]
        [tictactoe.player-factory :only [build-player]]
        ))

(defn main []
  (let [ui-handler (new-cli-handler)
        game-parameters (get-game-parameters ui-handler)
        player-1 (build-player (:x game-parameters) :x)
        player-2 (build-player (:o game-parameters) :o)]
    (main-loop player-1 player-2 ui-handler)))
(main)