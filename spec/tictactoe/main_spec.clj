(ns tictactoe.main-spec
  (:use [speclj.core]
        [tictactoe.main]))

(describe "menu functionality"
  (it "builds players out of strings"
    (should-not (build-player "74" :o))
    (should= tictactoe.smart-ai-player.SmartAiPlayer (type (build-player "1" :o)))
    (should= tictactoe.human-player.HumanPlayer (type (build-player "2" :o))))
  (it "builds players based on user input"
  (with-in-str "7\n5\n1"
    (should= tictactoe.smart-ai-player.SmartAiPlayer (type (get-player false :o)))))
  )