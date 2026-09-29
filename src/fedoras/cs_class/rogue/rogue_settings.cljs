;; ---------------------------------------------------------------------
;; ROGUE_SETTINGS.CLJS — everything in the game.
;;
;; Change a number and press Run. Add an item and it is in the shop.
;; Nothing here is checked against anything: an item that gives +900
;; Strength will give you +900 Strength, and the game will let you have
;; it, and you will stop playing about four minutes later.
;;
;; EFFECTS. Stats are added up. Flags are read by the fight.
;;
;;   :strength :max-hp :speed :dodge :crit   flat, added
;;   :energy-gain   extra energy per attack (5 is standard)
;;   :strong-at     energy needed for a strong attack
;;   :crit-mult     multiplier on a crit (2 is standard)
;;   :cleave        every attack hits every enemy
;;   :damage-mult   multiplies all damage you deal
;;   :lifesteal     heal this fraction of damage dealt
;;   :flat-steal    heal this many per attack that lands
;;   :double-strike attack twice, at :double-power of normal
;;   :gold-strength strength per ten gold held
;;   :item-strength strength per item held
;;   :level-strength strength per level
;;   :thorns        damage back to whoever hits you
;;   :soak          damage subtracted from every hit you take
;;   :reduce        fraction of damage you take, subtracted
;;   :regen         healed at the end of each round
;;   :fight-heal    healed at the start of each fight
;;   :won-heal      healed after each fight won
;;   :execute       fraction of enemy max hp below which they die outright
;;   :low-bonus     extra damage against enemies below half
;;   :first-bonus   extra damage on the first round of a fight
;;   :dodge-heal    healed whenever you dodge
;;   :gold-mult :xp-mult   multipliers on what you are given
;;   :start-energy  energy you begin each fight with
;;   :enemy-weaken  fraction off every enemy's health
;;   :discount      fraction off shop prices
;;
;; :name is optional everywhere. Without one, the description is the
;; name, which is how most of these were named anyway.
;; ---------------------------------------------------------------------

{:start {:hp 300 :strength 4 :speed 10 :dodge 0 :crit 5}

 :tuning {:energy-gain 5        ; energy per attack landed
          :strong-at 30         ; energy for a strong attack
          :strong-mult 2        ; and what it multiplies by
          :crit-mult 2
          :item-price 25
          :defend-reduce 0.5    ; damage taken while defending
          :enemy-gold 2
          :heal-price 10
          :heal-amount 12
          :paid-heal [14 20]    ; what the money buys
          :free-heal [3 7]      ; what sitting down buys
          :scavenge [4 11]
          :xp-curve [5 15 30 75 120 200 320]

          ;; THE SUPERBOSS. Every seventh floor the boss is something
          ;; else, and beating one at or above `fortune-level` is worth
          ;; a point of fortune. They keep coming. So does the game.
          :superboss-every 7
          :fortune-level 5

          ;; THE SHOP OUT THE BACK. Refreshing costs more every time and
          ;; works less often every time, and when it fails the shop is
          ;; shut and you have paid for that. This is a slot machine
          ;; with shelves drawn on it.
          :refresh-price 10
          :refresh-price-mult 2
          :refresh-chance 85
          :refresh-chance-drop 20

          ;; a fight finished under this fraction of your health is one
          ;; the log will mention, because nobody remembers those
          :near-miss 0.25}

 :floor {:steps 6              ; doors before the boss
         :encounters 3
         :shops 1
         :treasures 1
         :quests 2
         :empties 4}

  ;; STATUS. Poison ticks at the end of a round and does not
  ;; care what you are wearing. Shield takes damage before
  ;; health does and does not come back on its own.
 :poison-tick 1        ; damage per stack, each round
 :poison-fade 1        ; stacks lost at the end of a round

  ;; THE QUIET ROOM. Three ways out of it. Paying is the best
  ;; deal and the free heal is a third of it, which is the ratio
  ;; that makes the free one feel like a mistake either way.
 :heal-price 10
 :heal-amount 12
 :free-heal 4
 :scavenge 6           ; gold, if you take that instead

 :sacrifice-chance 50

 ;; -------------------------------------------------------------------
 ;; ITEMS. Bought, found, and taken off bosses.
 ;;
 ;; Everything stacks unless it says `:unique true`, and the things that
 ;; say so are the ones that set a threshold rather than add to a total.
 ;; Two of a thing that lowers your strong attack to 20 energy cannot
 ;; lower it to 10, so having two would be having one, and the game says
 ;; so rather than letting you find out.
 ;; -------------------------------------------------------------------
 :items
 [{:id :whetstone  :desc "+3 Strength"                        :strength 3}
  {:id :ration     :desc "+8 Max HP"                          :max-hp 8}
  {:id :boots      :desc "+4 Speed"                           :speed 4}
  {:id :cloak      :desc "+10% Dodge"                         :dodge 10}
  {:id :lens       :desc "+15% Crit"                          :crit 15}
  {:id :cell       :desc "+2 Energy per attack"               :energy-gain 2}
  {:id :leech      :desc "Heal 2 per attack that lands"       :flat-steal 2}
  {:id :ledger     :desc "+1 Strength per 10 gold"            :gold-strength 1}
  {:id :bramble    :desc "Reflect 3 damage"                   :thorns 3}
  {:id :anvil      :desc "+5 Strength, -2 Speed"              :strength 5 :speed -2}
  {:id :fork       :desc "One more attack at 60% damage"      :extra-strikes 1 :double-power 0.6}
  {:id :hook       :desc "+25% damage below half"             :low-bonus 0.25}
  {:id :flask      :desc "Heal 4 at the start of a fight"     :fight-heal 4}
  {:id :sigil      :desc "+2 Strength per level"              :level-strength 2}
  {:id :purse      :desc "+20% gold"                          :gold-mult 0.2}
  {:id :journal    :desc "+1 XP per kill"                     :xp-flat 1}
  {:id :plate      :desc "Take 2 less from every hit"         :soak 2}
  {:id :moss       :desc "Heal 3 each round"                  :regen 3}
  {:id :venom      :desc "Your attacks poison for 1"          :poison 1}
  {:id :barrier    :desc "Start each fight with 10 shield"    :shield 10}
  {:id :salve      :desc "Poison on you fades 1 faster"       :poison-fade 1}
  {:id :spur       :desc "+15% damage on the first round"     :first-bonus 0.15}
  {:id :tally      :desc "+8 Max HP per sacrifice held"       :sacrifice-hp 8}
  {:id :capacitor  :desc "Strong attack at 20 energy"         :strong-at 20 :unique true}
  {:id :prism      :desc "Crits deal 3x"                      :crit-mult 3 :unique true}]

 ;; -------------------------------------------------------------------
 ;; UPGRADES. Three offered on levelling, one taken.
 ;; -------------------------------------------------------------------
 :upgrades
 [{:id :u-frame    :desc "+10 Permanent HP"                   :max-hp 10}
  {:id :u-gait     :desc "+5 Permanent HP, +5 Speed"          :max-hp 5 :speed 5}
  {:id :u-arm      :desc "+4 Strength"                        :strength 4}
  {:id :u-eye      :desc "+20% Crit"                          :crit 20}
  {:id :u-shade    :desc "+15% Dodge"                         :dodge 15}
  {:id :u-bulk     :desc "+3 Strength, +10 Max HP"            :strength 3 :max-hp 10}
  {:id :u-charge   :desc "Start each fight with 15 energy"    :start-energy 15}
  {:id :u-sprint   :desc "+6 Speed"                           :speed 6}
  {:id :u-mend     :desc "Heal 6 after each fight"            :won-heal 6}
  {:id :u-opener   :desc "+30% damage on the first round"     :first-bonus 0.3}
  {:id :u-dynamo   :desc "+2 Strength, +2 Energy per attack"  :strength 2 :energy-gain 2}
  {:id :u-slip     :desc "Dodging heals 3"                    :dodge-heal 3}
  {:id :u-tithe    :desc "+50% gold"                          :gold-mult 0.5}
  {:id :u-hoard    :desc "+1 Strength per item held"          :item-strength 1}
  {:id :u-hide     :desc "Take 20% less damage"               :reduce 0.2}
  {:id :u-lesson   :desc "+2 XP per kill"                     :xp-flat 2}
  {:id :u-toxin    :desc "Your attacks poison for 2"          :poison 2}
  {:id :u-wall     :desc "Start each fight with 20 shield"    :shield 20}
  {:id :u-second   :desc "One more attack at 40% damage"      :extra-strikes 1 :double-power 0.4}
  {:id :u-grudge   :desc "+3 Strength per sacrifice held"     :sacrifice-strength 3}
  {:id :u-siphon   :desc "Heal 15% of damage dealt"           :lifesteal 0.15}
  {:id :u-coil     :desc "Strong attack at 20 energy"         :strong-at 20 :unique true}
  {:id :u-facet    :desc "+10% Crit, crits deal 2.5x"         :crit 10 :crit-mult 2.5 :unique true}
  {:id :u-vessel   :desc "+12 Max HP, and heal to full"       :max-hp 12 :heal-now true :unique true}
  {:id :u-rot      :desc "Enemies have 20% less health"       :enemy-weaken 0.2 :unique true}]

 ;; -------------------------------------------------------------------
 ;; SACRIFICES. Offered after a boss, and they are not balanced.
 ;;
 ;; `:cost` is what it takes in each currency. Which one it asks for
 ;; depends on what you have: an item or an upgrade first, then life,
 ;; then gold. So the richer you are the more it can take, and the
 ;; poorer you are the more it takes from you.
 ;; -------------------------------------------------------------------
 :sacrifices
 [{:id :s-wide   :desc "Every attack hits every enemy, at half damage"
   :cleave true :damage-mult 0.5
   :cost {:item 1 :upgrade 1 :life 12 :gold 40}}
  {:id :s-drink  :desc "Heal for half of all damage you deal"
   :lifesteal 0.5
   :cost {:item 1 :upgrade 1 :life 15 :gold 45}}
  {:id :s-edge   :desc "+50% Crit"
   :crit 50
   :cost {:item 1 :upgrade 1 :life 15 :gold 50}}
  {:id :s-again  :desc "Attack twice, at full damage"
   :double-strike true :double-power 1.0
   :cost {:item 2 :upgrade 1 :life 20 :gold 60}}
  {:id :s-greed  :desc "+10 Strength per 10 gold held"
   :gold-strength 10
   :cost {:item 1 :upgrade 2 :life 18 :gold 70}}
  {:id :s-study  :desc "Double all XP"
   :xp-mult 2
   :cost {:item 1 :upgrade 1 :life 10 :gold 40}}
  {:id :s-smoke  :desc "+40% Dodge"
   :dodge 40
   :cost {:item 1 :upgrade 1 :life 16 :gold 50}}
  {:id :s-surge  :desc "Every attack is a strong attack"
   :strong-at 0
   :cost {:item 2 :upgrade 2 :life 22 :gold 75}}
  {:id :s-swell  :desc "Double your maximum health"
   :hp-mult 2
   :cost {:item 1 :upgrade 2 :life 5 :gold 55}}
  {:id :s-finish :desc "Enemies below a quarter health die outright"
   :execute 0.25
   :cost {:item 2 :upgrade 1 :life 20 :gold 65}}
  {:id :s-plague :desc "Poison for 5, and poison spreads to everything"
   :poison 5 :poison-cleave true
   :cost {:item 1 :upgrade 2 :life 18 :gold 55}}
  {:id :s-again  :desc "One more attack at full damage"
   :extra-strikes 1 :double-power 1.0
   :cost {:item 2 :upgrade 1 :life 20 :gold 60}}]

 ;; -------------------------------------------------------------------
 ;; MINOR ENEMIES. Twenty. `:kind` is what the quests count.
 ;; -------------------------------------------------------------------
 :minors
 [{:id :m-roach      :name "ROACH"                 :kind :vermin  :cr 1 :hp 6  :strength 2 :speed 8}
  {:id :m-silverfish :name "SILVERFISH"            :kind :vermin  :cr 1 :hp 8  :strength 3 :speed 5}
  {:id :m-rat        :name "STAIRWELL RAT"         :kind :vermin  :cr 1 :hp 10 :strength 3 :speed 6}
  {:id :m-fresher    :name "FRESHER"               :kind :student :cr 1 :hp 12 :strength 4 :speed 5}
  {:id :m-email      :name "UNREAD EMAIL"          :kind :thing   :cr 1 :hp 7  :strength 8 :speed 9}
  {:id :m-mould      :name "SOMETHING IN THE VENT" :kind :vermin  :cr 2 :hp 14 :strength 2 :speed 3 :poison 2 :min-level 2}
  {:id :m-society    :name "SOCIETY RECRUITER"     :kind :student :cr 2 :hp 9  :strength 6 :speed 9}
  {:id :m-vending    :name "VENDING UNIT"          :kind :machine :cr 2 :hp 12 :strength 4 :speed 6}
  {:id :m-scanner    :name "CARD SCANNER"          :kind :machine :cr 2 :hp 10 :strength 6 :speed 8}
  {:id :m-groupwork  :name "GROUP PROJECT"         :kind :student :cr 2 :hp 20 :strength 3 :speed 4}
  {:id :m-tutor      :name "PEER TUTOR"            :kind :student :cr 2 :hp 15 :strength 5 :speed 6}
  {:id :m-adjunct    :name "ADJUNCT"               :kind :staff   :cr 3 :hp 18 :strength 5 :speed 5}
  {:id :m-invigil    :name "INVIGILATOR"           :kind :staff   :cr 3 :hp 14 :strength 7 :speed 5}
  {:id :m-turnstile  :name "TURNSTILE"             :kind :machine :cr 3 :hp 20 :strength 5 :speed 3}
  {:id :m-printer    :name "THE PRINTER"           :kind :machine :cr 3 :hp 25 :strength 3 :speed 2}
  {:id :m-queue      :name "THE QUEUE"             :kind :thing   :cr 3 :hp 26 :strength 4 :speed 3}
  {:id :m-deadline   :name "DEADLINE"              :kind :thing   :cr 3 :hp 18 :strength 7 :speed 10}
  {:id :m-porter     :name "NIGHT PORTER"          :kind :staff   :cr 4 :hp 16 :strength 6 :speed 7
   :extra-strikes 1 :double-power 0.5}
  {:id :m-registrar  :name "REGISTRAR'S CLERK"     :kind :staff   :cr 4 :hp 22 :strength 4 :speed 4 :shield 10 :min-level 3}
  {:id :m-form       :name "FORM 17-B"             :kind :thing   :cr 4 :hp 30 :strength 2 :speed 2 :shield 15 :min-level 4}
  {:id :m-office     :name "OFFICE HOURS"          :kind :staff   :cr 4 :hp 24 :strength 3 :speed 4
   :summons :m-fresher :summon-every 2 :summon-count 1 :min-level 3}
  {:id :m-chain      :name "REPLY-ALL CHAIN"       :kind :thing   :cr 4 :hp 20 :strength 4 :speed 6
   :summons :m-email :summon-every 3 :summon-count 2 :min-level 4}
  {:id :m-hearing    :name "DISCIPLINARY HEARING"  :kind :staff   :cr 5 :hp 34 :strength 6 :speed 6
   :extra-strikes 2 :double-power 0.4 :min-level 4}]

 ;; -------------------------------------------------------------------
 ;; BOSSES. Ten. One a floor, and the seventh is the last.
 ;; -------------------------------------------------------------------
 :bosses
 [{:id :b-dean    :name "SUB-DEAN OF INTAKE"     :kind :staff   :hp 45 :strength 7  :speed 12}
  {:id :b-audit   :name "THE AUDIT"              :kind :thing   :min-level 1 :hp 60 :strength 6 :speed 12 :shield 25}
  {:id :b-help    :name "A HELP, ESCALATED"      :kind :machine :hp 55 :strength 8  :speed 12}
  {:id :b-tenure  :name "TENURE COMMITTEE"       :kind :staff   :hp 70 :strength 7  :speed 12}
  {:id :b-orient  :name "ORIENTATION"            :kind :student :hp 65 :strength 9  :speed 12}
  {:id :b-loop    :name "THE LOOP, DERAILED"     :kind :machine :hp 80 :strength 9  :speed 12}
  {:id :b-fill    :name "WHAT IS UNDER THE FILL" :kind :thing   :min-level 4 :hp 95 :strength 12 :speed 12 :poison 3}
  {:id :b-alumni  :name "ALUMNI RELATIONS"       :kind :staff   :hp 75 :strength 10 :speed 12}
  {:id :b-14      :name "BUILDING 14"            :kind :thing   :hp 90 :strength 8  :speed 14}
  {:id :b-provost :name "THE PROVOST"            :kind :staff   :hp 110 :strength 13 :speed 12}]

           ;; WHO YOU MEET. An encounter has a budget in CR, and enemies
          ;; are drawn until it is spent. So one heavy thing and six
          ;; light ones are both a fair room, and a summoner has
          ;; somewhere to put what it produces.
 :encounter-cr [4 7]   ; at floor 1, level 1
 :cr-per-floor 1.6
 :cr-per-level 0.9
 :max-party 6
 :level-reach 2
 :min-enemy-growth 2
 :summon-cap 6

  ;; -------------------------------------------------------------------
 ;; SUPERBOSSES. Every seventh floor, in order, then round again. They
 ;; scale with the floor like everything else, so the second time you
 ;; meet one it is not the same meeting.
 ;; -------------------------------------------------------------------
 :superbosses
 [{:id :x-review  :name "THE REVIEW"           :kind :thing   :min-level 5 :hp 160 :strength 14 :speed 13}
  {:id :x-endow   :name "THE ENDOWMENT"        :kind :staff   :min-level 6 :hp 200 :strength 16 :speed 13}
  {:id :x-cohort  :name "THE STAKEHOLDERS"     :kind :student :min-level 7 :hp 240 :strength 15 :speed 14}
  {:id :x-charter :name "THE DIRECTOR"         :kind :thing   :min-level 8 :hp 300 :strength 18 :speed 15}]

 ;; -------------------------------------------------------------------
 ;; QUESTS. Two at a time. Turned in at a quest room once done.
 ;; -------------------------------------------------------------------
 :quests
 [{:id :q-vermin  :desc "Kill 5 vermin"        :goal :kill :kind :vermin  :count 5}
  {:id :q-staff   :desc "Kill 4 staff"         :goal :kill :kind :staff   :count 4}
  {:id :q-machine :desc "Kill 4 machines"      :goal :kill :kind :machine :count 4}
  {:id :q-student :desc "Kill 4 students"      :goal :kill :kind :student :count 4}
  {:id :q-thing   :desc "Kill 3 things"        :goal :kill :kind :thing   :count 3}
  {:id :q-boss    :desc "Beat a boss"          :goal :boss  :count 1}
  {:id :q-bosses  :desc "Beat two bosses"      :goal :boss  :count 2}
  {:id :q-items   :desc "Hold 3 items at once" :goal :items :count 3}
  {:id :q-hoard   :desc "Hold 5 items at once" :goal :items :count 5}
  {:id :q-upgrade :desc "Hold 4 upgrades"      :goal :upgrades :count 4}
  {:id :q-rich    :desc "Hold 60 gold at once" :goal :gold  :count 60}
  {:id :q-clean   :desc "Win a fight untouched" :goal :clean :count 1}]

 :quest-rewards {:gold 30 :xp 4}}