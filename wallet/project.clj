(defproject wallet "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
    [org.clojure/clojure "1.11.1"]
    [http-kit "2.3.0"]
    [com.brunobonacci/mulog "0.9.0"]
    [compojure "1.7.0"]
    [org.clojure/data.json "2.4.0"]
    [ring/ring-defaults "0.3.2"]
    [ring/ring-json "0.5.0"]
    [environ "1.2.0"]
    [org.clojure/java.jdbc "0.7.11"]
    [org.postgresql/postgresql "42.3.1"]]
  :plugins [[lein-environ "1.2.0"]]
  :main wallet.core
  :profiles {:uberjar {:aot :all :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
