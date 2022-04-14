(ns courses-bot.core
  (:require [clojure.core.async :refer [<!!]]
            [clojure.string :as str]
            [environ.core :refer [env]]
            [morse.handlers :as h]
            [morse.polling :as p]
            [morse.api :as t]
            [courses-bot.token :refer [token]]
            [courses-bot.message-commands :refer [message-commands]]
            [clojure.java.io :as io])
  (:gen-class))

;; (def token (env :telegram-token))


(h/defhandler handler

  (h/command-fn "start"
    (fn [{{id :id :as chat} :chat :as message}]
      (println "Bot joined new chat: " chat)
      (println "messge" message)
      (t/send-text token id "Welcome to courses-bot!")))

  (h/command-fn "help"
    (fn [{{id :id :as chat} :chat}]
      (println "Help was requested in " chat)
      (t/send-text token id "Help is on the way")))

  (h/message-fn
    (fn [{{id :id} :chat :as message}]

      ;; ((message-commands (:text message)) message)

      (println "Intercepted message: " message)
      (let [resp-func (message-commands (:text message))])
      (if-let [resp-func (message-commands (:text message))]
        (t/send-text token id (resp-func message))
        (t/send-text token id (:text message)))
      ;; (t/send-text token id ((message-commands (:text message)) message))
      )))

(defonce app-chan (atom nil))


(defn -main
  [& args]
  (when (str/blank? token)
    (println "Please provde token in TELEGRAM_TOKEN environment variable!")
    (System/exit 1))

  (println "Starting the courses-bot")
  (<!! (p/start token handler)))

(defn start-app
  []
  (reset! app-chan (p/start token handler)))

(defn stop-app
  []
  (p/stop @app-chan)
  (reset! app-chan nil))

(defn restart-app
  []
  (println "App is restarting")
  (stop-app)
  (start-app))

(comment
  (start-app)
  (stop-app)
  (restart-app)
  ;; отправка аудио из файла
  (def audio (t/send-audio token 37521589
                           (io/file (io/resource "courses/test.mp3")))

    )
  ;; отправка файла по file_id
  (t/send-document token 37521589 "CQACAgIAAxkDAAPlYiIYDG_jNaXZ1CCOR3vCg9J5yzkAApwVAAI2axBJ1bdPTKp9oXkjBA")

  (let [directory (io/file (io/resource "courses"))
        files (file-seq directory)]
    ;; (clojure.pprint/pprint (type files))
    (doseq [file files]
      ;; (println (.isDirectory file))
      ;; (println (.getParent file))
      (println file)
      ))


(let [directory (clojure.java.io/file (io/resource "courses"))
      dir? #(.isDirectory %)]
    (println (map #(.getPath %)
         (filter (comp not dir?)
                 (tree-seq dir? #(.listFiles %) directory)))))

  )

(defn create! )


(defn create-node! [file parent init])
(defn create-leaf! [file parent])

(defn walk
  [file parent]
  (cond)
  (if (.isDirectory file)
    (create-node! file parent false)
    (create-leaf! file parent))
  )


;; итак, какая структура мне нужна
{:name "dirnamee"
 :type "node"
 :path "/shit/piss/pray"
 :file "fileobj"
 :children [{:name "audio.mp3"
             :type "leaf"
             :file "fileobj"
             :path "/shit/piss/pray"}
            {:name "bukkake"
             :type "node"
             :path "/shit/piss/pray"
             :children []}
            ]}

(defn create-node2!
  [file parent]
  )
