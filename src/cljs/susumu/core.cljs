(ns cljs.susumu.core
  (:require [schema.core :as s :include-macros true]
            [goog.dom :as gdom]
            [goog.events :as gev]
            [cljc.susumu.core :as sc]))

;; generic

(s/defn current-location :- s/Str
  []
  (-> js/window .-location .-href))

(s/defn oget :- s/Any
  ([o :- js/Object
    k :- s/Keyword
    & ks :- [s/Keyword]]
   (apply aget o (sc/as-?name k) (map sc/as-?name ks)))
  ([o :- js/Object]
   o))

;; debug

(defn- tag-str
  [tag]
  (str "Tag: " tag " ->"))

(s/defn ->println :- s/Any
  ([tag :- s/Str
    x   :- s/Any]
   (do
     (println (tag-str tag) x)
     x))
  ([x :- s/Any]
   (do
     (println x)
     x)))

(s/defn ->log :- s/Any
  ([tag :- s/Str
    x   :- s/Any]
   (do
     (.log js/console (tag-str tag) x)
     x))
  ([x :- s/Any]
   (do
     (.log js/console x)
     x)))

;; event

(s/defn ev->target :- js/Element
  [ev :- js/Event]
  (oget ev :currentTarget))

(s/defn ev->value :- s/Str
  [ev :- js/Event]
  (oget (ev->target ev) :value))

(s/defn ev->id :- s/Str
  [ev :- js/Event]
  (oget (ev->target ev) :id))

(s/defn ev-prevent-default :- js/Event
  [ev :- js/Event]
  (do
    (.preventDefault ev)
    ev))

(s/defn ev->path :- s/Str
  [ev :- js/Event]
  (oget (ev->target ev) :pathname))

(s/defschema ReturnedListen
  {:node js/Element
   :events (s/cond-pre s/Str [s/Str])
   :callback-fn js/Function})

(s/defn listen :- ReturnedListen
  [node        :- js/Element
   events      :- (s/cond-pre s/Str [s/Str])
   callback-fn :- js/Function]
  (do
    (gev/listen node
                (if (vector? events)
                  (apply array events)
                  events)
                callback-fn)
    {:node node
     :events events
     :callback-fn callback-fn}))

(s/defn unlisten :- ReturnedListen
  [node        :- js/Element
   events      :- (s/cond-pre s/Str [s/Str])
   callback-fn :- js/Function]
  (do
    (gev/unlisten node
                  (if (vector? events)
                    (apply array events)
                    events)
                  callback-fn)
    {:node node
     :events events
     :callback-fn callback-fn}))

;; element

(s/defn id->el :- js/Element
  [id :- s/Str]
  (gdom/getElement id))
