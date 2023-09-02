(ns clojuregol.grid)

(defn coord->index
  "Get a linearized coordinate from grid gr at coordinates i, j"
  [gr i j]
  (->
   (:height gr)
   (* i)
   (+ j)))

(defn index->coord
  "Get i, j coordinates from linearized coordinates"
  [gr lin]
  {:i (quot lin (:height gr))
   :j (mod lin (:height gr))})

(defn get-elem
  "Get an element from coordinates i, j from grid gr"
  [gr, i, j]
  (get (:field gr) (coord->index gr i j)))

(defn generate-noisy-grid
  "Return a ixj grid filled with randomly alive cells according to probability prob"
  [i j prob]
  {:width i
   :height j
   :field (vec (repeatedly (* i j) #(if (< (rand) prob) :alive :dead)))})

(defn neighbor-states
  "Get states of surrounding cells"
  [gr i j] 
  (let [pairs (for [x1 [-1 0 1]
                    x2 [-1 0 1]]
                (vector x1 x2))]
    (->>
     pairs
     (map #(vector (+ i (first %)) (+ j (second %))))
     (filter #(not (or (and (= i (first %)) (= j (second %)))
                       (< (first %) 0) (< (second %) 0)
                       (>= (first %) (:width gr)) (>= (second %) (:height gr)))))
     (map #(if (= (get-elem gr (first %) (second %)) :alive) 1 0))
     (reduce +)))
  )

(defn alive-or-dead
  "Determine wether the cell should stay alive or die"
  [gr lin]
  (let [coords (index->coord gr lin)
        i (:i coords)
        j (:j coords)
        alive-neighbors (neighbor-states gr i j)]
    (if (or 
         (and (= (get-elem gr i j) :alive) (or (= alive-neighbors 2) (= alive-neighbors 3))) 
         (and (= (get-elem gr i j) :dead) (= alive-neighbors 3)))
      :alive
      :dead)))

(defn run-gol-step
  "Run a Game Of Life step through the field gr"
  ([gr]
   (run-gol-step gr [] 0))
  ([gr new-field field-ctn]
   (if (= field-ctn (* (:width gr) (:height gr)))
     (assoc gr :field new-field)
     (recur gr (conj new-field (alive-or-dead gr field-ctn)) (inc field-ctn)))))
