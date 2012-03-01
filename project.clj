(defproject tictactoe "1.0.0-SNAPSHOT"
  :description "Immutable Tic Tac Toe"
  :dependencies [[org.clojure/clojure "1.2.0"]]
  :dev-dependencies [[speclj "2.1.1"]]
  :test-path "spec/"
  :main tictactoe.main
  :aot [tictactoe.minimax]
)
