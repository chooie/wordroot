(set-env!
  :source-paths #{"src/frontend"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [adzerk/boot-cljs "1.7.228-1"]
                  [adzerk/boot-cljs-repl "0.3.2"]
                  [adzerk/boot-reload "0.4.12"]
                  [com.cemerick/piggieback "0.2.1"]
                  [pandeiro/boot-http "0.7.3"]
                  [weasel "0.7.0"]])

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]])

(deftask dev
  "Launch Immediate Feedback Development Environment"
  []
  (comp
    (serve :dir "target")
    (watch)
    (reload)
    (cljs-repl)
    (cljs)
    (target :dir #{"target"})))
