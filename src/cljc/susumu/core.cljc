(ns cljc.susumu.core
  (:require [clojure.string :as string]
            [schema.core :as s #?@(:cljs [:include-macros true])]
            #?(:cljs [goog.math :as gmath])))

;; type

(def Fn #?(:clj clojure.lang.IFn
           :cljs js/Function))

;; generic

(s/defn map-kvs :- {s/Any s/Any}
  [m :- {s/Any s/Any}
   f :- Fn]
  (persistent! (reduce-kv (fn [m k v]
                            (let [[k v] (f k v)]
                              (assoc! m k v)))
                          (transient {})
                          m)))

(s/defn dissoc-in :- {s/Any s/Any}
  [m :-  {s/Any s/Any}
   ks :- [s/Any]
   & dissoc-ks :- [s/Any]]
  (update-in m ks (fn [m]
                    (apply dissoc m dissoc-ks))))

(s/defn find-first :- s/Any
  [f    :- Fn
   coll :- [s/Any]]
  (first (filter f coll)))

(s/defn nnil? :- s/Bool
  [x :- s/Any]
  (not (nil? x)))

(s/defn nempty? :- s/Bool
  [x :- (s/cond-pre [s/Any] {s/Any s/Any})]
  (not (empty? x)))

(s/defn nblank? :- s/Bool
  [s :- s/Str]
  (not (string/blank? s)))

(s/defn nneg? :- s/Bool
  [x :- s/Num]
  (not (neg? x)))

(s/defn npos? :- s/Bool
  [x :- s/Num]
  (not (pos? x)))

(s/defn nzero? :- s/Bool
  [x :- s/Num]
  (not (zero? x)))

(s/defn bool? :- s/Bool
  [x :- s/Any]
  (or (true? x) (false? x)))

(s/defn nvec? :- s/Bool
  [x :- s/Any]
  (not (vector? x)))

(s/defn nmap? :- s/Bool
  [x :- s/Any]
  (not (map? x)))

(s/defn as-?set :- #{s/Any}
  [x]
  (cond
   (set? x) x
   (coll? x) (set x)))

(s/defn in :- s/Any
  [coll :- (s/cond-pre [s/Any] {s/Any s/Any})
   x    :- s/Any]
  ((as-?set coll) x))

(s/defn as-?kw :- (s/maybe (s/cond-pre s/Keyword s/Str))
  [x :- s/Any]
  (cond
   (keyword? x) x
   (string? x) (keyword x)))

(s/defn as-?name :- (s/maybe (s/cond-pre s/Str s/Keyword))
  [x :- s/Any]
  (cond
    (string? x) x
    (keyword? x) (name x)))

(s/defn as-?email :- (s/maybe s/Str)
  "Taken from https://github.com/ptaoussanis/encore"
  [x :- s/Str]
  (when x (re-find #"^[^\s@]+@[^\s@]+\.\S*[^\.]$" (string/trim x))))

(s/defn nnil= :- s/Bool
  "Taken from https://github.com/ptaoussanis/encore"
  ([x :- s/Any
    y :- s/Any]
   (and (nnil? x) (= x y)))
  ([x :- s/Any
    y  :- s/Any
    & more :- [s/Any]]
   (and (nnil? x) (apply = x y more))))

(s/defn contains?* :- s/Bool
  [coll  :- s/Any
   ks    :- [s/Keyword]]
  (every? #(contains? coll %) ks))

;; math

(s/defn round :- s/Num
  [x :- s/Num]
  #?(:clj (Math/round (double x))
     :cljs (Math/round x)))

(s/defn abs :- s/Num
  [x :- s/Num]
  (if (neg? x)
    (- x)
    x))

(s/defn pow :- s/Num
  [x        :- s/Num
   power-by :- s/Num]
  (Math/pow x power-by))

(s/defn floor :- s/Int
  [x :- s/Num]
  #?(:clj (int (Math/floor x))
     :cljs (gmath/safeFloor x)))

(s/defn ceil :- s/Int
  [x :- s/Num]
  #?(:clj (int (Math/ceil x))
     :cljs (gmath/safeCeil x)))
