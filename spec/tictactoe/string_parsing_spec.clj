(ns tictactoe.string-parsing-spec
  (:use [speclj.core]
        [tictactoe.string-parsing]))

(describe "String parsing"
  (it "parses player string"
    (should= :x (string-to-player "x"))
    (should= :o (string-to-player "o"))
    (should-not (string-to-player "z")))
  (it "parses board"
    (should=
      [[:x nil :x]
       [:o nil :o]
       [:x nil :o]]
      (string-to-board "x xo ox o")))

  )