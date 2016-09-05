 (ns clj-sm2.unit-tests
   (:require
     [doo.runner :refer-macros [doo-tests]]
     [clj-sm2.core-test]))

 (doo-tests
   'clj-sm2.core-test)
