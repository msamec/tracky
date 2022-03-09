(ns tracky.presentation.templates.index
  (:require [presentation.templates.base :refer [base]]
            [hiccup.form :refer [form-to]]
            [domain.date :refer [seconds->duration]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn render [entries credential]
  (base
   [:main.container.mx-w-6xl.mx-auto.py-4
    [:div.flex.flex-col
     [:div.overflow-x-auto.sm:-mx-6.lg:-mx-8
      [:div.py-2.inline-block.min-w-full.sm:px-6.lg:px-8
       [:div.overflow-hidden
        (if (empty? entries)
          [:div.container.mx-auto
           [:div.max-w-xl.p-5.mx-auto.my-10.bg-white.rounded-md.shadow-sm
            [:span.inline-flex.px-2
             "You are up to date! :)"]]]
          [:table.min-w-full
           [:thead.bg-white.border-b
            [:tr
             [:th.text-sm.font-medium.text-gray-900.px-6.py-4.text-left
              {:scope "col"} "Jira task id"]
             [:th.text-sm.font-medium.text-gray-900.px-6.py-4.text-left
              {:scope "col"} "Description"]
             [:th.text-sm.font-medium.text-gray-900.px-6.py-4.text-left
              {:scope "col"} "Duration"]
             [:th.text-sm.font-medium.text-gray-900.px-6.py-4.text-left
              {:scope "col"} "Start date"]
             [:th.text-sm.font-medium.text-gray-900.px-6.py-4.text-left
              {:scope "col"} (form-to [:post (str "/sync-all-tasks")]
                                      (anti-forgery-field)
                                      [:button.bg-blue-500.hover:bg-blue-700.text-white.py-1.px-2.rounded "Sync All"])]]]
           [:tbody
            (for [{:keys [id task-id description duration start-date start-time]} entries]
              [:tr.bg-white.border-b.transition.duration-300.ease-in-out.hover:bg-gray-100
               [:td.text-sm.text-gray-900.font-light.px-6.py-4.whitespace-nowrap task-id]
               [:td.text-sm.text-gray-900.font-light.px-6.py-4.whitespace-nowrap description]
               [:td.text-sm.text-gray-900.font-light.px-6.py-4.whitespace-nowrap (seconds->duration duration)]
               [:td.text-sm.text-gray-900.font-light.px-6.py-4.whitespace-nowrap start-date] ;TODO format date
               [:td.text-sm.text-gray-900.font-light.px-6.py-4.whitespace-nowrap
                (form-to [:post (str "/sync-task/" id)]
                         (anti-forgery-field)
                         [:button.bg-green-500.hover:bg-green-700.text-white.py-1.px-2.rounded "Sync"])]])]])]]]
     [:div.container.mx-auto
      [:div.max-w-xl.p-5.mx-auto.my-10.bg-white.rounded-md.shadow-sm
       [:div
        (form-to [:post "/update-credential"]
                 (anti-forgery-field)
                 [:div.mb-6
                  [:label.block.mb-2.text-sm.text-gray-600
                   {:for "options[toggl-key]"}
                   "Toggl api key"]
                  [:input.w-full.px-3.py-2.placeholder-gray-300.border.border-gray-300.rounded-md.focus:outline-none.focus:ring.focus:ring-indigo-100.focus:border-indigo-300
                   {:required "required",
                    :name "options[toggl-key]",
                    :value (get-in credential [:options :toggl-key] nil)
                    :type "text"}]]
                 [:div.mb-6
                  [:label.block.mb-2.text-sm.text-gray-600
                   {:for "options[tempo-key]"}
                   "Tempo api key"]
                  [:input.w-full.px-3.py-2.placeholder-gray-300.border.border-gray-300.rounded-md.focus:outline-none.focus:ring.focus:ring-indigo-100.focus:border-indigo-300
                   {:required "required",
                    :value (get-in credential [:options :tempo-key] nil)
                    :name "options[tempo-key]",
                    :type "text"}]]
                 [:div.mb-6
                  [:label.block.mb-2.text-sm.text-gray-600
                   {:for "options[jira-account-id]"}
                   "Jira account id"]
                  [:input.w-full.px-3.py-2.placeholder-gray-300.border.border-gray-300.rounded-md.focus:outline-none.focus:ring.focus:ring-indigo-100.focus:border-indigo-300
                   {:required "required",
                    :name "options[jira-account-id]",
                    :value (get-in credential [:options :jira-account-id] nil)
                    :type "text"}]]
                 [:div.mb-6
                  [:div.mb-6
                   [:button.w-full.px-2.py-4.text-white.bg-indigo-500.rounded-md.focus:bg-indigo-600.focus:outline-none
                    {:type "submit"}
                    "Update"]]])]]]]]))
