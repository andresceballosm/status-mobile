(ns status-im2.contexts.shell.share.events
  (:require [utils.re-frame :as rf]
            [status-im2.common.toasts.events :as toasts]
            [quo2.foundations.colors :as colors]))

(rf/defn copy-text-and-show-toast
  {:events [:share/copy-text-and-show-toast]}
  [{:keys [db] :as cofx} {:keys [text-to-copy post-copy-message]}]
  (rf/merge cofx
            {:copy-to-clipboard text-to-copy}
            (toasts/upsert
             {:icon           :correct
              :id             :successful-copy-toast-message
              :icon-color     colors/success-50
              :override-theme :dark
              :text           post-copy-message})))
