(ns pong.ball
  (:require [reagent.core :as reagent :refer [atom cursor]]
            [pong.config :as c]))

(def position
  (atom {:y {:pos (/ (:board-height c/dims) 2) :speed 0}
         :x {:pos (/ (:board-width  c/dims) 2)} :speed 0}))

(def base-speed 30)
(def sqrt (.-sqrt js/Math))

(def speed-x (cursor position [:x :speed]))
(def speed-y (cursor position [:y :speed]))

(def pos-x   (cursor position [:x :pos]))
(def pos-y   (cursor position [:y :pos]))

(defn- speed-x->y
  [speed-x base-speed]
  (int
   (sqrt (- (* base-speed 2)
            (* speed-x speed-x)))))

(defn- init-speed!
  "Init random x- and y- components:
   2*base-speed^2 = speed-x^2 + speed-y^2"
  []
  (let [sign (fn [] (rand-nth [-1 1]))
        x (* (sign) (rand-int (/ base-speed 2)))
        y (* (sign) (speed-x->y x base-speed))]
    (reset! speed-x x)
    (reset! speed-y y)))

(init-speed!) ;; TODO: get rid of this

(defn collisions [{:keys [:pos :speed] :as p} lborder rborder] ""
  (let [rpred (if rborder (>= pos rborder) false)
        lpred (if lborder (<= pos lborder) false)]
    (if (or lpred rpred) {:pos pos :speed (- speed)} p)))

(defn fail? [{:keys [:pos :speed] :as p}]
  (if (< pos 0) {:pos 0 :speed 0} p))

(defn new-pos
  [{:keys [:pos :speed] :as p}]
  (update p :pos #(+ speed pos)))

(defn move!
  "Move ball."
  []
  (let [{:keys [:board-width :board-height :ball-size]} c/dims
        pos-x (:x @position)
        pos-y (:y @position)
        new-x (-> pos-x
                  (collisions 0 (- board-width ball-size))
                  (fail?)
                  (new-pos))
        new-y (-> pos-y
                  (collisions 0 (- board-height ball-size))
                  (new-pos))]
    (if (or (= (:speed new-x) 0) (= (:speed new-y) 0))
      (js/console.log "Fail!")
      (do
  ;;      (js/console.log (clj->js new-x))
        (swap! position assoc :x new-x)
        (swap! position assoc :y new-y)))))

(defn ball
  "Ball's React component"
  []
  [:div#ball {:style {:top @pos-y
                      :left @pos-x}}])
