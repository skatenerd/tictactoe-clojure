(ns tictactoe.game-runner
  (:use [tictactoe.board-utils :only [empty-board update-board game-over? game-winner]]
        [tictactoe.move-source]
        [tictactoe.ui-handler]))

(defn main-loop
  [active-player inactive-player board ui-handler]
  (let [active-player-move (next-move active-player board ui-handler)
        new-board (update-board board active-player-move (.signature active-player))
        continue-game? (not (game-over? new-board))]
    (if continue-game?
      (recur inactive-player active-player new-board ui-handler)
      (farewell ui-handler new-board (game-winner new-board)))))
;  ([active-player inactive-player board-size ui-handler]
;    (main-loop active-player inactive-player empty-board ui-handler))

