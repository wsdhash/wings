(ns wallet.database
    (:require   
    [clojure.java.jdbc :as jdbc]
    [wallet.configs :refer [db-configs]]))

(defn create-tables-if-not-exists []
    (jdbc/execute! db-configs [
        "CREATE TABLE IF NOT EXISTS users (
            user_id VARCHAR(255) NOT NULL PRIMARY KEY)"])
    
    (jdbc/execute! db-configs [
        "CREATE TABLE IF NOT EXISTS balances (
            id SERIAL PRIMARY KEY,
            user_id VARCHAR(255) REFERENCES users(user_id), 
            balance FLOAT DEFAULT 10)"]))

(defn create-user-and-balance-if-not-exists
  [user_id] 
  (jdbc/execute! db-configs ["INSERT INTO users (user_id) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_id = ?)", user_id user_id])
  (jdbc/execute! db-configs ["INSERT INTO balances (user_id) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM balances WHERE user_id = ?)", user_id user_id]))

(defn get-balance
    [user_id]
    (jdbc/execute! db-configs ["SELECT balance FROM balances WHERE user_id = ? LIMIT 1" user_id]))