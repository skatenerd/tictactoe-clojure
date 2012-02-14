(ns tictactoe.ui-silencer-spec
(:use [speclj.core]
      [tictactoe.ui-silencer]))

(describe "call-recording"
  (with ui-handler (new-ui-silencer))
  (it "remembers its calls in a queue"
    (farewell @ui-handler :foo :biz :baz)
  (should= (list "farewell") @(.calls @ui-handler))))
