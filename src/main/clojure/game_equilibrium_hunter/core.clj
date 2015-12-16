(ns game-equilibrium-hunter.core
  (:gen-class))

; TODO:
;  (1) graph a single variance series to show convergance (or non-convergance)
;  (2) graph for various n to show how convergance gets harder to acheive
;  (3) consider better convergance analysis, (cycles indicate convergance but show variance)
;  (4) automatically identify convergance and stop early
;  (5) tests and doc strings

(defn make-player [n]
  (vec (repeat n n)))

(defn choose
  ([player]
   (let [n (count player)]
     (choose player (rand-int (* n n)) 0)))
  ([player pick n]
   (let [weight (first player)]
     (if (<= pick weight)
       n
       (recur (rest player) (- pick weight) (inc n))))))


(defn pick [player]
  (rand-int (count player)))

(defn move-weight [player n m]
  (let [from-weight    (nth player n)
        taking         (if (< 0 from-weight) 1 0)
        reduced-weight (- from-weight taking)
        reduced        (assoc player n reduced-weight)
        to-weight      (nth reduced m)
        increased-weight (+ to-weight taking)]
    (assoc reduced m increased-weight)))

(defn weaken [player n]
  (move-weight player n (pick player)))

(defn reinforce [player n]
  (move-weight player (pick player) n))



(defn make-game [n]
  (into {}
        (for [a (range n)
              b (range n)]
          [[a b] (* (rand) (rand))])))



(defn play-game
  ([n] (play-game (make-game n) (make-player n) (make-player n)))
  ([g p1 p2]
   (let [p1-strategy (choose p1)
         p2-strategy (choose p2)
         p1-outcome (g [p1-strategy p2-strategy])
         p2-outcome (g [p2-strategy p1-strategy])]
     (if (= p1-outcome p2-outcome)
       [g p1 p2]
       (let [p1-wins (> p1-outcome p2-outcome)
             p2-wins (> p2-outcome p1-outcome)
             p1-learns ((if p1-wins reinforce weaken) p1 p1-strategy)
             p2-learns ((if p2-wins reinforce weaken) p2 p2-strategy)]
         [g p1-learns p2-learns])))))

(defn tournament
  ([n]
   (let [round (play-game n)]
     (lazy-seq (cons round (apply tournament round)))))
  ([g p1 p2]
   (let [round (play-game g p1 p2)]
     (lazy-seq (cons round (apply tournament round))))))

(defn avg [s]
  (let [sum       (apply + s)
        size      (count s)]
        (/ sum size)))

(defn sqr-diff [mean n]
  (let [diff (- n mean)]
    (* diff diff)))

(defn variance [s]
  (let [mean      (avg s)
        sqr-diffs (map #(sqr-diff mean %) s)]
    (avg sqr-diffs)))

(defn variances [rounds]
  (map variance (apply (partial map vector) rounds)))

(defn tournament-stability
  ([n]
   (tournament-stability (* n n) (map (fn [[_ p1 p2]] [p1 p2]) (tournament n))))
  ([window t]
   (let [p1-rounds (take window (map first t))
         p2-rounds (take window (map second t))
         variance  (avg (concat (variances p1-rounds) (variances p2-rounds)))]
     (lazy-seq (cons variance (tournament-stability window (rest t)))))))


(defn -main
  [& args]
  (let [n 4]
    (println "shows the possible convergance to a Nash equilibrium of a random game of" n "strategies:")
    (println (take 10 (drop 1000 (tournament-stability n))))))