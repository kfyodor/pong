(ns pong.game
  (:require [reagent.core :refer [atom]]
            [pong.ball :as b]))

(def game-state (atom {:running false}))

(defn- game-running?
  []
  (not= (:running @game-state) false))

(defn start!
  []
  (let [loop (:running @game-state)]
    (when (= false loop)
      (swap! game-state
             assoc
             :running
             (js/setInterval b/move! (int (/ 1000 60)))))))

(defn pause!
  []
  (when-let [loop (:running @game-state)]
    (js/clearInterval loop)
    (swap! game-state assoc :running false)))

(defn controls
  "Start/stop button's React component."
  []
  (let [running (game-running?)
        caption (if running "Pause!" "Start!")
        action  (if running #(pause!) #(start!))]
    [:div.controls
     [:a {:on-click action} caption]]))
