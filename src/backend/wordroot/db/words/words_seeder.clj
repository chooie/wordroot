(ns wordroot.db.words.words-seeder
  (:require
   [wordroot.db.words.words :as words]))

(defn seed!
  []
  (words/persist-word!
    {:word    "thisisareallylongword"
     :meaning "Some meaning about a really long word"
     :parts   [{:part "this"
                :root {:word     "root1"
                       :meaning  "meaning1"
                       :language "language1"}}
               {:part "is"}
               {:part "a"}
               {:part "really"
                :root {:word     "root2"
                       :meaning  "meaning2"
                       :language "language2"}}
               {:part "long"}
               {:part "word"
                :root {:word     "root3"
                       :meaning  "meaning3"
                       :language "language3"}}]}))
