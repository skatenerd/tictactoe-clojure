(ns tictactoe.player-factory
  (:use [tictactoe.smart-ai-player :only [new-smart-ai-player]]
        [tictactoe.human-player :only [new-human-player]]
        [tictactoe.dumb-ai-player :only [new-dumb-ai-player]]))

(defn build-player [input signature]
  (case input
    :unbeatable-ai
    (new-smart-ai-player signature)
    :dumb-ai
    (new-dumb-ai-player signature)
    :human
    (new-human-player signature)
    ))