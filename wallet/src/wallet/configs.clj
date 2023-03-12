(ns wallet.configs
    (:require [environ.core :refer [env]]))

(println (env :dbhost))

(def api-configs {
  :port (-> env :port (or "8080") (Integer/parseInt))})

(def db-configs {
  :dbtype   "postgresql"
  :host     (-> env :dbhost (or "127.0.0.1"))
  :port     (-> env :dbport (or "5432") (Integer/parseInt))
  :dbname   (-> env :dbname)
  :user     (-> env :dbuser)
  :password (-> env :dbpass)})
