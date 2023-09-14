(defproject clojuregol "0.1.0"
  :description "Game of Life in Clojure and Raylib"
  :url "https://github.com/games-of-life/clojuregol"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-raylib "0.0.1"]]
  :main ^:skip-aot clojuregol.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
