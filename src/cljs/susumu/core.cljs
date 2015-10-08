(ns cljs.susumu.core
  (:require [schema.core :as s :include-macros true]
            [goog.dom :as gdom]
            [goog.events :as gev]))

;; etc

(s/defn current-location :- s/Str
  []
  (-> js/window .-location .-href))

;; debug

(s/defn ->println :- s/Any
  [x :- s/Any]
  (do
    (println x)
    x))

(s/defn ->log :- s/Any
  [x :- s/Any]
  (do
    (.log js/console x)
    x)

;; event

(s/defn ev->target :- js/Element
  [ev :- js/Event]
  (.-currentTarget ev))

(s/defn ev->value :- s/Str
  [ev :- js/Event]
  (.-value (el->target ev)))

(s/defn ev->id :- s/Str
  [ev :- js/Event]
  (.-id (el->target ev)))

(s/defn ev-prevent-default :- js/Event
  [ev :- js/Event]
  (do
    (.preventDefault ev)
    ev))

(s/defn ev->path :- s/Str
  [ev :- js/Event]
  (.-pathname (ev->target ev)))

(s/defschema ReturnedListen
  {:node js/Element
   :callback-fn js/Function})

(s/defn listen :- ReturnedListen
  [node        :- js/Element
   events      :- js/Event
   callback-fn :- js/Function]
  (gev/listen node
              (if (vector? events)
                (apply array events)
                events)
              callback-fn))

(s/defn unlisten :- ReturnedListen
  [node        :- js/Element
   events      :- js/Event
   callback-fn :- js/Function]
  (gev/unlisten node
                (if (vector? events)
                  (apply array events)
                  events)
                callback-fn))

;; element

(s/defn id->el :- js/Element
  [id :- s/Str]
  (gdom/getElement id))
