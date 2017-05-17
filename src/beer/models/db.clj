(ns beer.models.db
  (:require [clojure.java.jdbc :as sql]
            [clojure.string :as str]
            [korma.core :refer :all]
            [korma.db :refer :all]
            [clojure.set :as cs]
            [clj-time.coerce :as c]
            [clj-time.core :as t])
  (:import java.sql.DriverManager))

(defdb db (mysql {:db "beer"
                     :user "admin"
                     :password "admin"}))
(defentity user
  (table :user)
  (entity-fields :id :username :first_name :last_name :email :role))

(defentity beer-style
  (table :beer_style))

(defentity beer
  (table :beer))

(defentity beer-like
  (table :beer_like))

(defentity beer-comment
  (table :beer_comment)
  (belongs-to user))

(defn add-user [params]
  (insert user
  (values params)))

(defn get-user [id]
  (select user
          (fields :username :password :first_name :last_name :email)
  (where {:id id})))

(defn delete-user [id]
  (delete user
  (where {:id id})))

(defn find-user [params]
  (select user
  (where params)))

(defn get-users []
  (select user
  (where {:role "user"})
  (order :id :ASC)))

(defn update-user [id username password first-name last-name email]
  (println id username password first-name last-name email)
  (update user
          (set-fields {:username username :password password :first_name first-name :last_name last-name :email email})
          (where {:id id})))

(defn search-users [text]
  (select user
     (where (and {:role "user"} (or (like :username text) (like :first_name text) (like :email text))))
          (order :id :ASC)))

(defn add-beer [beer-name origin price beer-style alcohol manufacturer country info picture]
  (insert beer
  (values {:name beer-name :origin origin :price price :beer_style beer-style :alcohol alcohol :manufacturer manufacturer :country country :info info :picture picture})))

(defn update-beer [id beer-name origin price beer-style alcohol manufacturer country info picture]
  (update beer
          (set-fields {:name beer-name :origin origin :price price :beer_style beer-style :alcohol alcohol :manufacturer manufacturer :country country :info info :picture picture})
          (where {:id id})))

(defn delete-beer [id]
  (delete beer
  (where {:id id})))

(defn get-beers []
  (select beer
  (order :id :ASC)))

(defn search-beers [text]
 (select beer
  (where (or {:id text} (like :name text)
             (:origin text) (:price text) (:beer_style text) (like :alcohol text) (like :country text)
             (like :manufacturer text) (like :country text) (like :info text)))
         (order :id :ASC)))

(defn get-beer-styles []
  (select beer-style
          (order :id :ASC)))

(defn update-beer-style [params]
  (update beer-style
          (set-fields
            {:description (:description params)})
          (where {:id (:id params)})))

(defn search-beer-styles [text]
 (select beer-style
  (where (or {:id text} (like :name text) (like :description text)))
         (order :id :ASC)))

(defn find-beer-style-by-id [id]
  (select beer-style
  (where {:id id})))

(defn find-beer-style-by-name [bs-name]
  (select beer-style
  (where {:name bs-name})
          (order :id :ASC)))

(defn find-beer-by-id [id]
(select beer
        (fields :* [:beer_style.name :beer_style_name])
        (join beer-style (= :beer_style :beer_style.id))
  (where {:id id})))

(defn get-beers-for-beer-style [bs-id]
  (select beer
          (where {:beer_style bs-id})
          (order :beer.id :ASC)))

(defn find-beer-by-beer-style-origin-price [bs-id origin price]
  (select beer
          (where {:beer_style bs-id :origin origin :price price})
          (order :beer.id :ASC)))

(defn add-beer-like [user-id beer-id]
  (insert beer-like
  (values {:user_id user-id :beer_id beer-id})))

(defn delete-beer-like [user-id beer-id]
  (delete beer-like
  (where {:user_id user-id :beer_id beer-id})))

(defn get-beer-likes [beer-id]
  (select beer-like
  (where {:beer_id beer-id})))

(defn find-user-like-for-beer [beer-id user-id]
  (select beer-like
  (where {:beer_id beer-id :user_id user-id})))

(defn add-beer-comment [user-id beer-id text]
  (insert beer-comment
  (values {:user_id user-id :beer_id beer-id :comment text :date (c/to-sql-time (t/now))})))

(defn get-beer-comments [beer-id]
  (select beer-comment
          (with user)
          (where {:beer_id beer-id})
          (order :id :ASC)))

(defn delete-beer-comment [id]
  (delete beer-comment
  (where {:id id})))
