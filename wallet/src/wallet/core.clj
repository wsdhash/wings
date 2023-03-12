(ns wallet.core
  (:gen-class)
  (:require 
    [org.httpkit.server :refer [run-server]]
    [com.brunobonacci.mulog :as u]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :refer [not-found ]]
    [clojure.data.json :as json]
    [ring.middleware.json :as mj]
    [wallet.configs :refer [api-configs db-configs]]
    [clojure.java.jdbc :as jdbc]
    [wallet.database :refer [
      create-tables-if-not-exists 
      create-user-and-balance-if-not-exists
      get-balance]]))

(defn wrap-http-log
  [handler]
  (fn [request]
    (let 
      [path (:uri request) method (:request-method request) remote-addr (:remote-addr request)]
      (u/log ::REQ :method method :path path :remote-addr remote-addr))
    (handler request)
  )
)

(defn wrap-verify-authorization 
  [handler]
  (fn [request] 
    (let 
      [headers (:headers request) user_id (get headers "user-id")] 
      (if (not (nil? user_id)) 
        (create-user-and-balance-if-not-exists user_id)
        (println user_id)
      )
      (handler request)
    )
  )
)

(defn route-balance-account
  [request] 
  (
    let [headers (:headers request) user_id (get headers "user-id")]
    {
      :status 200
      :headers {"Content-Type" "application/json"}
      :body (json/write-str {:balance (first (get-balance user_id))})
    }
  )
)

(defroutes routes
  (GET "/balance" [] route-balance-account)
  (not-found "<h1>Not found</h1>"))

(def api 
  (wrap-http-log (wrap-verify-authorization routes)))

(defn -main []
  (create-tables-if-not-exists)
  (u/start-publisher! {:type :console})
  (u/log ::http ::start api-configs)
  (println db-configs)
  (run-server api api-configs))