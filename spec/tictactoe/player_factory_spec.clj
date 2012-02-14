(ns tictactoe.player-factory-spec
  (:use [speclj.core]
        [tictactoe.player-factory]))

(describe "menu functionality"
  (it "builds players based on user input"
    (should= tictactoe.human-player.HumanPlayer (type (build-player :human :x)))
    (should= tictactoe.smart-ai-player.SmartAiPlayer (type (build-player :unbeatable-ai :o)))
    (should= tictactoe.dumb-ai-player.DumbAiPlayer (type (build-player :dumb-ai :x)))
    (should= tictactoe.medium-ai-player.MediumAiPlayer (type (build-player :medium-ai :x)))

  ))