(ns tictactoe.medium-ai-player-spec
  (:use [tictactoe.medium-ai-player]
        [speclj.core]
        [tictactoe.ui-silencer]
        [tictactoe.move-source]))

(describe "move choices"

  (with x-can-win-row [[:x :x nil]
                       [:o :o nil]
                       [:x :o nil]])

  (with o-can-win-row [[:o  :o  nil]
                       [:x  nil nil]
                       [nil :x  nil]])

  (with x-can-win-col [[:x   nil :o]
                       [:x   :o  nil]
                       [nil nil nil]])

  (with x-can-win-smart [[:o :x  nil]
                         [:x nil nil]
                         [:o nil nil]])

  (with x-can-win-really-smart [[:x  nil :o ]
                                [nil nil nil]
                                [nil nil nil]])



  (with medium-ai-player (new-medium-ai-player :x))

  (with ui-handler (new-ui-silencer))

  (context "generating moves"

    (it "moves based on imminent victories"
      (should= [0 2] (next-move @medium-ai-player @x-can-win-row @ui-handler))
      (should= [2 0] (next-move @medium-ai-player @x-can-win-col @ui-handler)))

    (it "blocks opponent wins"
      (should= [0 2] (next-move @medium-ai-player @o-can-win-row @ui-handler)))

    (it "moves based on easy guaranteed victories"
      (should= [1 1] (next-move @medium-ai-player @x-can-win-smart @ui-handler)))

    (it "sometimes fails to see very distant victories"
      (let [moves (set (map (fn [_] (next-move @medium-ai-player @x-can-win-really-smart @ui-handler)) (range 50)))]
        (should (< 3 (count moves)))))))