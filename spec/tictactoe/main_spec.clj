(ns tictactoe.main-spec
  (:use [speclj.core]
        [tictactoe.main]
        [tictactoe.dumb-ai-player :only [new-dumb-ai-player]]
        [tictactoe.human-player :only [new-human-player]]))

(describe "menu functionality"
  (it "builds players out of strings"
    (should-not (build-player "74" :o))
    (prn (type (new-dumb-ai-player :o)))
    (should= tictactoe.dumb-ai-player.DumbAIPlayer (type (build-player "1" :o)))
    (should= tictactoe.human-player.HumanPlayer (type (build-player "2" :o))))
  (it "builds players based on user input"
  (with-in-str "7\n5\n1"
    (should= tictactoe.dumb-ai-player.DumbAIPlayer (type (get-player false :o)))))
  )