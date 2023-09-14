(ns clojuregol.core
  (:require [clj-raylib.core :as rl]
            [clojuregol.grid :as g])
  (:gen-class))

(def width 800)
(def height 600)

(def box-dimension 10)

(defn -main
  [& _]
  (let [box-width (/ width box-dimension)
        box-height (/ height box-dimension)]

    (rl/init-window! width height "Game of life")
    ;; (rl/set-target-fps! 30)

    (loop [gr {:width box-width
               :height box-height
               :field (g/random-field box-width box-height 0.5)}]
      (when-not (rl/window-should-close?)
        (rl/with-drawing
          (rl/clear-background! rl/BLACK)
          (doseq [i (range box-width)
                  j (range box-height)]
            (rl/draw-rectangle! (* box-dimension i) (* box-dimension j)
                                (- box-dimension 1) (- box-dimension 1)
                                (if (contains? (:field gr) [i j]) rl/WHITE rl/BLACK)))
          (rl/draw-fps! 20 20))
        (recur (g/run-gol-step gr))))
    

    (rl/close-window!)))
