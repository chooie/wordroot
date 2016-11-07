(ns wordroot-tasks.ide-integration
  (:require
   [boot.core :as boot]))

(boot/deftask cider
  "CIDER profile"
  []
  (require 'boot.repl)
  (swap! @(resolve 'boot.repl/*default-dependencies*)
    concat '[[org.clojure/tools.nrepl "0.2.12"]
             [cider/cider-nrepl "0.13.0"]
             [refactor-nrepl "2.2.0"]])
  (swap! @(resolve 'boot.repl/*default-middleware*)
    concat '[cider.nrepl/cider-middleware
             refactor-nrepl.middleware/wrap-refactor])
  identity)
