(ns pong.ball
  (:require [reagent.core :as reagent :refer [atom]]
            [pong.config :as c]))

(def position
  (atom {:y (/ (:board-height c/dims) 2)
         :x (/ (:board-width  c/dims) 2)}))

(def speed
  (clojure.core/atom {:x 0
                      :y 0}))

;; speed = x-speed + y-speed
;; speed^2 = x-speed^2 + y-speed^2
;; speed

(def base-speed 15)
(def sqrt (.-sqrt js/Math))

(defn mirror!
  [key]
  (swap! speed update key -))

(defn init-speed!
  []
  (let [sign (fn [] (rand-nth [-1 1]))
        x (* (sign) (rand base-speed))
        y (* (sign) (sqrt (- (* base-speed 2) (* x x))))]
    (reset! speed {:x x :y y})))

(init-speed!)

(defn delta
  [c-key binding]
  (let [pos (c-key @position)
        speed (c-key @speed)
        new-pos (+ pos speed)
        left (+ new-pos (:ball-size c/dims))]
    (when (or (<= new-pos 0) (>= left binding))
      (mirror! c-key))
    speed))

(defn move!
  []
  (let [{:keys [:board-width :board-height]} c/dims
        dx (delta :x board-width)
        dy (delta :y board-height)]
    (swap! position update :x (fn [i] (+ dx i)))
    (swap! position update :y (fn [i] (+ dy i)))))

(defn ball
  []
  (let [y (@position :y)
        x (@position :x)]
    [:div#ball {:style {:top y :left x}}]))
