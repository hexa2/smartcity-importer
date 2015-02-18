(defproject smartcity-importer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clojurewerkz/quartzite "2.0.0"]
                 [capacitor "0.4.2"]]
  :main ^:skip-aot smartcity-importer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
