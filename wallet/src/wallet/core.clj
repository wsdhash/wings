; crie um
(ns wallet.core
  (:gen-class)
  (:require 
    [org.httpkit.server :refer [run-server]]
    [com.brunobonacci.mulog :as u]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :refer [not-found]]
    [clojure.data.json :as json]
    [ring.middleware.json :as mj]))

(defn http-log
  [request]
  (let [
    path (:uri request) 
    method (:request-method request)
    remote-addr (:remote-addr request)
  ] (u/log ::REQ :method method :path path :remote-addr remote-addr)))

(defn balance-account
  [request] 
  (http-log request)
  {
    :status 200
    :headers {"Content-Type" "application/json"}
    :body (json/write-str {:balance 1000})
  })

(defn create-account
  [request]
  (http-log request)
  (let [data (get-in request [:body])]  {
    :status 200
    :headers {"Content-Type" "application/json"}
    :body (json/write-str {:body data })
  }))

(defroutes routes
  (GET "/balance" [] balance-account)
  (POST "/account" [] 
    (mj/wrap-json-body create-account))
  (not-found "<h1>Not found</h1>"))

(defn -main []
  (u/start-publisher! {:type :console})
  (u/log ::http ::start "Running on http://127.0.0.1:3521")
  (run-server routes {:port 3521}))