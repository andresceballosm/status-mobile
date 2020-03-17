(ns status-im.hardwallet.wallet
  (:require [status-im.ethereum.core :as ethereum]
            [status-im.utils.fx :as fx]
            [status-im.hardwallet.common :as common]
            [status-im.constants :as constants]
            [status-im.ethereum.eip55 :as eip55]
            [status-im.ui.components.bottom-sheet.core :as bottom-sheet]))

(fx/defn show-pin-sheet
  {:events [:hardwallet/new-account-pin-sheet]}
  [{:keys [db] :as cofx} sheet-options]
  (fx/merge
   cofx
   {:db (assoc-in db [:hardwallet :pin :enter-step] :export-key)}
   (bottom-sheet/show-bottom-sheet sheet-options)))

(fx/defn hide-pin-sheet
  {:events [:hardwallet/hide-new-account-pin-sheet]}
  [{:keys [db] :as cofx}]
  (fx/merge
   cofx
   {:utils/dispatch-later
    ;; We need to give previous sheet some time to be fully hidden 
    [{:ms 200
      :dispatch [:wallet.accounts/generate-new-keycard-account]}]}
   (bottom-sheet/hide-bottom-sheet)))

(fx/defn generate-new-keycard-account
  {:events [:wallet.accounts/generate-new-keycard-account]}
  [cofx]
  (common/show-connection-sheet
   cofx
   {:on-card-connected :wallet.accounts/generate-new-keycard-account
    :handler
    (fn [{:keys [db]}]
      (let [path-num (inc (get-in db [:multiaccount :latest-derived-path]))
            path     (str constants/path-wallet-root "/" path-num)
            pin      (common/vector->string (get-in db [:hardwallet :pin :export-key]))
            pairing  (common/get-pairing db)]
        {:db
         (assoc-in
          db [:hardwallet :on-export-success]
          #(vector :wallet.accounts/account-stored
                   {;; Strip leading 04 prefix denoting uncompressed key format
                    :address    (eip55/address->checksum (str "0x" (ethereum/public-key->address (subs % 2))))
                    :public-key (str "0x" %)
                    :path       path}))

         :hardwallet/export-key {:pin pin :pairing pairing :path path}}))}))
