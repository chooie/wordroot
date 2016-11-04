(ns wordroot.db.words.example-words)

(def words
  [{:word    "thisisareallylongword"
    :meaning "Some meaning about a really long word"
    :parts   [{:part "this"
               :root {:word     "root1"
                      :meaning  "meaning1"
                      :language "language1"}}
              {:part "is"}
              {:part "a"}
              {:part "really"
               :root {:word     "root2"
                      :meaning  "meaning2"
                      :language "language2"}}
              {:part "long"}
              {:part "word"
               :root {:word     "root3"
                      :meaning  "meaning3"
                      :language "language3"}}]}
   {:word    "atestword"
    :meaning "A test word meaning"
    :parts   [{:part "a"
               :root {:word     "root4"
                      :meaning  "meaning4"
                      :language "language4"}}
              {:part "test"
               :root {:word     "root5"
                      :meaning  "meaning5"
                      :language "language5"}}
              {:part "word"
               :root {:word     "root3"
                      :meaning  "meaning3"
                      :language "language3"}}]}
   {:word    "reallylengthyword"
    :meaning "A really lengthy word description"
    :parts   [{:part "really"
               :root {:word     "root2"
                      :meaning  "meaning2"
                      :language "language2"}}
              {:part "lengthy"
               :root {:word     "root6"
                      :meaning  "meaning6"
                      :language "language6"}}
              {:part "word"
               :root {:word     "root3"
                      :meaning  "meaning3"
                      :language "language3"}}]}])
