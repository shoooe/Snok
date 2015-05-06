(ns snok.view
  (:use [seesaw.core]
        [seesaw.graphics]))

; Creates a canvas that draws the state.
(defn make-canvas [state draw-fn]
  (canvas
    :background :black
    :paint 
      (fn [_ gcxt]
        (anti-alias gcxt)
        (draw-fn gcxt @state))))

; Makes the timer that updates the state and
; calls a repaint on the canvas.
(defn make-timer [state canv update-fn]
  (let [delta 100]
    (timer
      (fn [_]
        (swap! state (partial update-fn delta))
        (repaint! canv))
      :delay delta)))

; Generates the frames that contains the 
; canvas.
(defn make-frame [canv]
  (frame 
    :title "Snok" 
    :width 900 :height 400
    :content canv
    :on-close :exit
    :resizable? false))

; Creates the frame and calls `update-fn` every once in a while
; passing the delta time and the current state and expects the new state
; in return. The `draw-fn` is then called with the current context and
; current state in order to draw the state.
(defn main-loop [update-fn draw-fn ini-state]
  (let [state (atom ini-state)
        canv (make-canvas state draw-fn)
        tmr (make-timer state canv update-fn)
        frm (make-frame canv)]
    (show! frm)))
