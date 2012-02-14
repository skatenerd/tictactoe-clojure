(ns tictactoe.player-factory
  (:use [tictactoe.smart-ai-player :only [new-smart-ai-player]]
        [tictactoe.human-player :only [new-human-player]]))

(defn build-player [input signature]
  (case input
    :ai
    (new-smart-ai-player signature)
    :human
    (new-human-player signature)))