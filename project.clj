(defproject tracky "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [duct/core "0.8.0"]
                 [duct/module.logging "0.5.0"]
                 [duct/module.sql "0.6.1"]
                 [duct/module.web "0.7.3"]
                 [org.postgresql/postgresql "42.2.19"]
                 [nl.mediquest/duct.module.reitit "1.0.3"]
                 [hiccup/hiccup "1.0.5"]
                 [duct/migrator.ragtime "0.3.2"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.2"]
                 [magnet/buddy-auth.jwt-oidc "0.10.3"]
                 [duct/middleware.buddy "0.2.0"]
                 [com.verybigthings/penkala "8f38814dcfe5a23ee2c6fcdd5d2c48ccd6f4f1c7"]
                 [clj-jwt/clj-jwt "0.1.1"]]
  :plugins [[duct/lein-duct "0.12.3"]
            [reifyhealth/lein-git-down "0.4.1"]
            [lein-cljfmt "0.8.0"]]
  :repositories [["public-github" {:url "git://github.com"}]]
  :git-down {com.verybigthings/penkala {:coordinates retro/penkala}}
  :main ^:skip-aot tracky.main
  :uberjar-name  "tracky-standalone.jar"
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware     [lein-duct.plugin/middleware
                   lein-git-down.plugin/inject-properties]
  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :uberjar {:aot :all}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[integrant/repl "0.3.2"]
                                   [hawk "0.2.11"]
                                   [eftest "0.5.9"]
                                   [kerodon "0.9.1"]
                                   [lambdaisland/kaocha "1.64.1010"]]
                  :aliases {"kaocha" ["run" "-m" "kaocha.runner"]}}})
