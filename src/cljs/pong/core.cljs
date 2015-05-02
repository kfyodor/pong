(ns pong.core
    (:require [reagent.core :as reagent :refer [atom cursor]]
              [pong.ball :as b]
              [pong.game :as g]
              [pong.config :as c]
              [cljs.core.async :refer [put! chan <!]]))

(defn- px
  [i]
  (str i "px"))

(defn board
  []
  [:div#board {:style {:width (px (:board-width c/dims))
                       :height (px (:board-height c/dims))}}
   [b/ball]])

(defn field
  []
  [:div#field
   [:h1 "(pong)"]
   [board]
   [g/controls]])

(reagent/render [field]
                (.getElementById js/document "app"))
