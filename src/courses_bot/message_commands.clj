(ns courses-bot.message-commands)

(def message-commands
  {"Hello" (fn [message] "Привет!")
   "Bye" (fn [message] "Пока!")})
