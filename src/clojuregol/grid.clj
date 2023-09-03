(ns clojuregol.grid)

;; adapted from http://clj-me.cgrand.net/2011/08/19/conways-game-of-life/
(defn random-field 
  "initialize a random field with size*prob alive cells"
  [w h prob]
  (set (for [x (repeatedly  (* w h prob) #(vector (rand-int w) (rand-int h)))]
         x)))

(defn- moore-neighborhood 
  "Calculate Moore neighborhood of a point"
  [[x y]]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not (= [dx dy] [0 0]))]
    [(+ x dx) (+ y dy)]))

(defn- filter-out-of-bounds 
  "Takes in Moore neighborhood and filters out out-of-bounds cells"
  [neighborhood w h]
  (filter (fn [[x y]] (not (or (< x 0) (< y 0) (>= x w) (>= y h)))) neighborhood))

(defn run-gol-step 
  "Progress Game of Life a step forward"
  [set-of-cells w h]
  (set (for [[cell count] (frequencies (filter-out-of-bounds (mapcat moore-neighborhood set-of-cells) w h))
             :when (or (= 3 count)
                       (and (= 2 count) (contains? set-of-cells cell)))]
         cell)))
