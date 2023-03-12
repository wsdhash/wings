(ns wallet.configs
    (:require [environ.core :refer [env]]))

(def api-configs {
  :host (-> env :host)
  :port (-> env :port Integer/parseInt)})

(def db-configs {
  :dbtype   "postgresql"
  :host     (-> env :db-host)
  :port     (-> env :db-port Integer/parseInt)
  :dbname   (-> env :db-name)
  :user     (-> env :db-user)
  :password (-> env :db-pass)})