(ns tictactoe.player-factory-spec
  (:use [speclj.core]
        [tictactoe.player-factory]))

(describe "menu functionality"
  (it "builds players based on user input"
    (should= tictactoe.human-player.HumanPlayer (type (build-player :human :x)))
    (should= tictactoe.smart-ai-player.SmartAiPlayer (type (build-player :ai :o))))
  )