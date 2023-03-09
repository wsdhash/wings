(ns wallet.core
  (:gen-class)
  (:require 
    [org.httpkit.server :refer [run-server]]
    [com.brunobonacci.mulog :as u]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :refer [not-found ]]
    [clojure.data.json :as json]
    [ring.middleware.json :as mj]))

; Log API events.
(defn wrap-http-log
  [handler]
  (fn [request]
    (let 
      [path (:uri request) method (:request-method request) remote-addr (:remote-addr request)]
      (u/log ::REQ :method method :path path :remote-addr remote-addr))
    (handler request)
  )
)

; Check if the user has authorization
; to access this feature.
(defn wrap-verify-authorization 
  [handler]
  (fn [request] 
    (let [headers (:headers request) user (get headers "x-user-id")] (println user))
    (handler request)
  )
)

(defn balance-account
  [request] 
  {
    :status 200
    :headers {"Content-Type" "application/json"}
    :body (json/write-str {:balance 1000})
  }
)

(defn create-account
  [request]
  (let [data (get-in request [:body])]  {
    :status 200
    :headers {"Content-Type" "application/json"}
    :body (json/write-str {:body data })
  })
)

(defroutes routes
  (GET "/balance" [] balance-account)
  (POST "/account" [] 
    (mj/wrap-json-body create-account))
  (not-found "<h1>Not found</h1>"))

(def api 
  (wrap-verify-authorization (wrap-http-log routes)))

(defn -main []
  (u/start-publisher! {:type :console})
  (u/log ::http ::start "Running on http://127.0.0.1:3521")
  (run-server api {:port 3521}))